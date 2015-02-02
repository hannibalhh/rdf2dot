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

    public void RDF2Dot(String rdfPath,RDFFormat format,String dotPath) throws IOException, RDFParseException, RDFHandlerException {
        serializer = new Graphviz2DotSerializer(new GraphConfiguration());
        parser     = Rio.createParser(format);
        parser.setRDFHandler(new GraphvizHandler(serializer));
        parser.parse(this.getClass().getResourceAsStream(rdfPath), "");
        File f = new File(dotPath);
        f.setWritable(true);
        f.createNewFile();
        PrintWriter out = new PrintWriter(dotPath);
        out.println(serializer.toDot());
        out.close();
    }
}
