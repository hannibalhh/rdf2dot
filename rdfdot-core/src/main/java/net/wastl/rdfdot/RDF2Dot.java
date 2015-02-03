package net.wastl.rdfdot;

import net.wastl.rdfdot.config.GraphConfiguration;
import net.wastl.rdfdot.render.Graphviz2DotSerializer;
import org.openrdf.rio.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RDF2Dot {
    private Logger logger = Logger.getLogger("RDF2Dot");
    private Graphviz2DotSerializer serializer;
    private RDFParser parser;
    public final Path rdfPath;

    public RDF2Dot(String rdfPath,RDFFormat format) throws IOException, RDFParseException, RDFHandlerException {
        this.rdfPath = Paths.get(rdfPath);
        serializer = new Graphviz2DotSerializer(new GraphConfiguration());
        parser     = Rio.createParser(format);
        parser.setRDFHandler(new GraphvizHandler(serializer));
    }

    public void toDot(String dotPath) throws IOException{
        toDot(rdfPath,Paths.get(dotPath));
    }
    public void toDot(Path rdfPath,Path dotPath) {
        logger.info("convert: " +  rdfPath +" -> " + dotPath);
        try {
            parser.parse(new FileInputStream(rdfPath.toFile()),"");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RDFParseException e) {
            e.printStackTrace();
        } catch (RDFHandlerException e) {
            e.printStackTrace();
        }
        File f = dotPath.toFile();
        f.setWritable(true);
        try {
            dotPath.getParent().toFile().mkdirs();
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter out = null;
        try {
            out = new PrintWriter(f.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        out.println(serializer.toDot());
        out.close();
    }

    public void toDots(String dotPath,String extension) throws IOException{
        toDots(Paths.get(dotPath),extension);
    }
    public void toDots(Path dotPath,String extension) throws IOException {
        if (!(Files.isDirectory(dotPath) || !dotPath.isAbsolute())){
            logger.warning("dotPath is not a directory: " + dotPath);
            return;
        }
        if (!Files.isDirectory(rdfPath)) {
            logger.warning("rdfPath is not a directory: " + rdfPath);
            return;
        }
        List<Path> l = fileList(rdfPath,extension);
        l.forEach(filePath -> {
            toDot(filePath, concatName(targetPath(rdfPath, dotPath), filePath,extension));
        });
    }

    public Path concatName(Path target,Path filePath,String extension){
        return Paths.get(target.toFile().getPath(),filePath.getFileName().toFile().getName().replace("."+extension,".dot"));
    }

    public Path targetPath(Path rdfPath,Path dotPath){
        if (dotPath.isAbsolute())
            return dotPath;
        else
            return Paths.get(rdfPath.toFile().getPath(),dotPath.toFile().getPath());
    }
    public List<Path> fileList(Path path,String extension) throws IOException {
        List<Path> l = new ArrayList<Path>();
        Files.walk(path).forEach(filePath -> {
            if (Files.isRegularFile(filePath) && filePath.toString().toLowerCase().endsWith("."+extension)) {
                l.add(filePath);
            }
        });
        return l;
    }
}
