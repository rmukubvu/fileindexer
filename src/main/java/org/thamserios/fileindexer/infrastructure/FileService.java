package org.thamserios.fileindexer.infrastructure;



import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.FSDirectory;


import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;


public class FileService {

    //format the file to have spaces
    public String reformatFileForIndexing(String fileName) throws IOException {
        File originalFile = new File(fileName);
        String newFileName = String.format("%s.new",fileName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(newFileName));
        LineIterator lineIterator = FileUtils.lineIterator(originalFile);
        try {
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if ( line.startsWith("*#")) {
                    writer.append(line + "\n");
                    continue;
                }
                writer.append(String.format("%s\t%s\n",line.substring(0,7),line.substring(7)));
            }
        } finally {
            lineIterator.close();
            writer.flush();
            writer.close();
        }
        return newFileName;
    }

    public void indexFile(String fileName) throws IOException, URISyntaxException {
        LuceneFileIndexer index = LuceneFileIndexer.builder(FSDirectory.open(Paths.get("tmp/indexing")), new StandardAnalyzer()).build();
        index.addFileToIndex(fileName);
    }

    public List<Document> search(String requestNo) throws IOException {
        LuceneFileIndexer index = LuceneFileIndexer.builder(FSDirectory.open(Paths.get("tmp/indexing")), new StandardAnalyzer()).build();
        return index.searchFiles(requestNo);
    }
}
