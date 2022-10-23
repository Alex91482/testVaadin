package org.example.service.xls;

import com.vaadin.server.DownloadStream;
import com.vaadin.server.FileResource;

import java.io.File;
import java.lang.reflect.Field;
import java.util.concurrent.Callable;

public class DynamicFileResource extends FileResource {

    protected Callable<String> processor;

    /** provide the source file using callback */
    public DynamicFileResource(Callable<String> processor) {
        super(new File("."));
        this.processor = processor;
    }

    @Override
    public DownloadStream getStream() {
        try {
            // process and set the resulted file before returning stream
            String filePath = processor.call();
            setSourceFile(new File(filePath));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to generate file resource", e);
        }
        return super.getStream();
    }

    // set sourceFile (maybe Vaadin team should make the method setSourceFile(File sourceFile) public..)
    public void setSourceFile(File sourceFile) {
        // access private member: FileResource.sourceFile
        try {
            Field f = FileResource.class.getDeclaredField("sourceFile");
            f.setAccessible(true); // force accessible
            f.set(this, sourceFile);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to set sourceFile", e);
        }
    }
}
