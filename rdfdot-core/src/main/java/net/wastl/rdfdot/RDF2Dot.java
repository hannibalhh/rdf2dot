package net.wastl.rdfdot;

import net.wastl.rdfdot.GraphvizHandler;
import net.wastl.rdfdot.config.GraphConfiguration;
import net.wastl.rdfdot.render.Graphviz2DotSerializer;
import org.openrdf.rio.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class RDF2Dot {
    private Graphviz2DotSerializer serializer;
    private RDFParser parser;

    public RDF2Dot(String rdfPath,RDFFormat format) throws IOException, RDFParseException, RDFHandlerException {
        serializer = new Graphviz2DotSerializer(new GraphConfiguration());
        parser     = Rio.createParser(format);
        parser.setRDFHandler(new GraphvizHandler(serializer));
        parser.parse(this.getClass().getResourceAsStream(rdfPath), "");
    }

    public void toDot(String dotPath) throws IOException {
        File f = new File(dotPath);
        f.setWritable(true);
        f.createNewFile();
        PrintWriter out = new PrintWriter(dotPath);
        out.println(serializer.toDot());
        out.close();
    }
}
