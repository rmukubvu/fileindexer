package org.thamserios.fileindexer.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Builder(builderMethodName = "defaultBuilder")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LuceneFileIndexer {

    private Directory indexDirectory;
    private StandardAnalyzer analyzer;

    public static LuceneFileIndexerBuilder builder(Directory fsDirectory, StandardAnalyzer analyzer) {
       return LuceneFileIndexer.defaultBuilder().indexDirectory(fsDirectory).analyzer(analyzer);
    }

    public void addFileToIndex(String filepath) throws IOException {

        Path path = Paths.get(filepath);
        File file = path.toFile();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);

        LineIterator lineIterator = FileUtils.lineIterator(new File(filepath));
        try {
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if ( line.startsWith("*#")) {
                    continue;
                }
                Document document = new Document();
                String[] split = line.split("\t");
                document.add(new TextField("requestNo", split[0] , Field.Store.YES));
                document.add(new TextField("lineContent", split[1] , Field.Store.YES));
                document.add(new StringField("filename", file.getName(), Field.Store.YES));
                indexWriter.addDocument(document);
            }
        } finally {
            lineIterator.close();
        }
        indexWriter.close();
    }

    public List<Document> searchFiles(String requestNo) {
        try {
            Query query = new QueryParser("requestNo", analyzer).parse(requestNo);
            Directory dir = FSDirectory.open(new File("tmp/indexing").toPath());
            IndexReader indexReader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(indexReader);
            TopDocs topDocs = searcher.search(query, 100);
            List<Document> documents = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc));
            }
            return documents;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

}
