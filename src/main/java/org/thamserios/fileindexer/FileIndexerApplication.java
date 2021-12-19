package org.thamserios.fileindexer;

import org.thamserios.fileindexer.infrastructure.FileService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;


public class FileIndexerApplication  {


    public static void main(String[] args) throws IOException, URISyntaxException {
        //SpringApplication.run(FileIndexerApplication.class, args);
        FileService fileService = new FileService();
        //String newFileName = fileService.reformatFileForIndexing("UI.G.UI.FN.LOG.AAB.LM02.X01.G1953V00");
        //System.out.println(newFileName);
        //fileService.indexFile("UI.G.UI.FN.LOG.AAB.LM02.X01.G1953V00.new");
        //System.out.println("done indexing");

        var document = fileService.search("0092126");
        AtomicInteger count = new AtomicInteger();
        document.stream().forEach(d -> {
            System.out.println(count.incrementAndGet() + ": 0092126" + d.get("lineContent"));
        });
        //System.out.println("done searching");
    }
/*
    @Override
    public void run(String... args) throws Exception {
        FileService fileService = new FileService();
        //String newFileName = fileService.reformatFileForIndexing("UI.G.UI.FN.LOG.AAB.LM02.X01.G1953V00");
        //System.out.println(newFileName);
        //fileService.indexFile("UI.G.UI.FN.LOG.AAB.LM02.X01.G1953V00.new");
        System.out.println("done indexing");

        var document = fileService.search("0092126");
        System.out.println("done searching");
    } */
}
