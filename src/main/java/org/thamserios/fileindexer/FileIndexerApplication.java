package org.thamserios.fileindexer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.thamserios.fileindexer.infrastructure.FileService;

import java.io.IOException;
import java.net.URISyntaxException;


public class FileIndexerApplication  {


    public static void main(String[] args) throws IOException, URISyntaxException {
        //SpringApplication.run(FileIndexerApplication.class, args);
        FileService fileService = new FileService();
        //String newFileName = fileService.reformatFileForIndexing("UI.G.UI.FN.LOG.AAB.LM02.X01.G1953V00");
        //System.out.println(newFileName);
        //fileService.indexFile("UI.G.UI.FN.LOG.AAB.LM02.X01.G1953V00.new");
        //System.out.println("done indexing");

        var document = fileService.search("0092126");
        document.stream().forEach(d -> {
            System.out.println("0092126" + d.get("lineContent"));
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
