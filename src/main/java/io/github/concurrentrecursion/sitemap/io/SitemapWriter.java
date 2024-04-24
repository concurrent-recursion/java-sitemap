package io.github.concurrentrecursion.sitemap.io;

import io.github.concurrentrecursion.exception.DataSerializationException;
import io.github.concurrentrecursion.sitemap.model.IndexSitemap;
import io.github.concurrentrecursion.sitemap.model.UrlSetSitemap;
import io.github.concurrentrecursion.sitemap.model.validation.WriteValidation;
import io.github.concurrentrecursion.sitemap.util.UrlUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

/**
 * The SitemapGenerator class is responsible for generating sitemap files and configuring various settings.
 */
@Data
@Accessors(chain = true)
@Slf4j
public class SitemapWriter implements Writer {
    private static final long MAX_FILESIZE = 50L * 1024L * 1024L;// 50MB in bytes
    private static final JAXBContext JAXB_CONTEXT;
    @Getter(AccessLevel.NONE)
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * The base URL of the website
     * @param baseUrl the base URL
     * @return the base URL
     */
    private URL baseUrl;
    /**
     * Whether the XML should be indented and have newlines
     * @param prettyPrint true to use formatted human readable, false for smaller file size.
     * @return true if pretty print is enabled, otherwise false
     */
    private boolean prettyPrint = false;
    /**
     * Specifies whether to use Gzip compression in the application.
     *
     * <p>
     * By default, Gzip compression is disabled. If set to {@code true},
     * the application will use Gzip compression to compress the response payloads.
     * </p>
     * <p>
     * Please note that enabling Gzip compression can help reduce the size of
     * the response payloads and improve the performance of the application,
     * especially when transmitting data over a network.
     * </p>
     *
     * <p>
     * To enable Gzip compression, set this variable to {@code true}.
     * </p>
     *
     * @param useGzip whether to use gzip
     * @return useGzip whether to use gzip
     */
    private boolean useGzip = false;
    /**
     * The variable filenamePrefix represents the prefix of a file name.
     * Its default value is {@code sitemap}
     *
     * @param filenamePrefix the filename without a file extension
     * @return the filename prefix
     * */
    private String filenamePrefix = "sitemap";

    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(UrlSetSitemap.class, IndexSitemap.class);
        } catch (JAXBException e) {
            throw new DataSerializationException(e);
        }
    }

    @Override
    public String writeToString(IndexSitemap index) {
        validate(index);
        try(StringWriter sw = new StringWriter()){
            marshal(index, sw);
            return sw.toString();
        }catch (IOException e){
            throw new DataSerializationException(e);
        }
    }

    @Override
    public void write(IndexSitemap index, Path file) throws IOException {
        validate(index);
        try(OutputStream os = getOutputStream(file)){
            marshal(index,os);
        }
    }


    @Override
    public String writeToString(UrlSetSitemap urlSet) {
        validate(urlSet);
        try(StringWriter sw = new StringWriter()){
            marshal(urlSet, sw);
            return sw.toString();
        }catch (IOException e){
            throw new DataSerializationException(e);
        }
    }

    private void validate(IndexSitemap index){
        Set<ConstraintViolation<IndexSitemap>> errors = validatorFactory.getValidator().validate(index, WriteValidation.class);
        if(!errors.isEmpty()){
            throw new ConstraintViolationException(errors);
        }
    }

    private void validate(UrlSetSitemap urlSet) {
        Set<ConstraintViolation<UrlSetSitemap>> errors = validatorFactory.getValidator().validate(urlSet, WriteValidation.class);
        Path tempFile = null;
        try{
            tempFile = Files.createTempFile("temp-urlset",".xml");

            getMarshaller().marshal(urlSet, Files.newOutputStream(tempFile));
            long bytes = Files.size(tempFile);
            log.debug("Temp Validation File:{}, bytes:{}",tempFile.toFile().getAbsoluteFile(),bytes);
            if(bytes > MAX_FILESIZE){
                double megabytes = bytes / 1024d / 1024d;
                throw new DataSerializationException(String.format("UrlSet is too big. File size is %.2fMB, Maximum file size is 50MB uncompressed",megabytes));
            }
        }catch (IOException|JAXBException e){
            log.error("Exception writing temp file for filesize check {}",tempFile,e);
        }finally {
            if(tempFile != null){
                try {
                    Files.deleteIfExists(tempFile);
                }catch (IOException e){
                    log.error("Exception deleting temp file: {}",tempFile,e);
                }
            }

        }

        if(!errors.isEmpty()){
            throw new ConstraintViolationException(errors);
        }
    }

    @Override
    public void write(UrlSetSitemap urlSet, Path file) throws IOException {
        validate(urlSet);
        if(useGzip && !(file.getFileName().endsWith(".gz") || file.getFileName().endsWith(".gzip"))){
            file = file.resolveSibling(file.getFileName() + ".gz");
        }
        urlSet.setFile(file);
        try(OutputStream os = getOutputStream(file)){
            marshal(urlSet,os);
        }
    }

    @Override
    public boolean getPrettyPrint() {
        return false;
    }

    @Override
    public Writer useGzipCompression(boolean gzip) {
        this.useGzip = gzip;
        return this;
    }

    @Override
    public boolean getUseGzipCompression() {
        return useGzip;
    }

    @Override
    public Writer setBaseUrl(String baseUrl) {
        this.baseUrl = UrlUtil.convertToUrl(baseUrl);
        return this;
    }

    @Override
    public Writer setBaseUrl(URL baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    private OutputStream getOutputStream(Path path) throws IOException {
        if(getUseGzipCompression()){
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(Files.newOutputStream(path));
            return new BufferedOutputStream(gzipOutputStream);
        }else{
            return new BufferedOutputStream(Files.newOutputStream(path));
        }
    }

    private Marshaller getMarshaller() throws JAXBException {
        Marshaller marshaller = JAXB_CONTEXT.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, prettyPrint);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT,true);
        marshaller.setProperty("org.glassfish.jaxb.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        return marshaller;
    }


    private void marshal(Object obj, java.io.Writer writer){
        try {
            getMarshaller().marshal(obj, writer);
        }catch (JAXBException e){
            throw new DataSerializationException(e);
        }
    }

    private void marshal(Object obj, OutputStream os){
        try {
            getMarshaller().marshal(obj, os);
        }catch (JAXBException e){
            throw new DataSerializationException(e);
        }
    }

}
