package net.wastl.rdfdot;

import junit.framework.Assert;
import net.wastl.rdfdot.config.GraphConfiguration;
import net.wastl.rdfdot.render.Graphviz2DotSerializer;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.rio.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

public class RDF2DotTest {

    private Graphviz2DotSerializer serializer;
    private RDFParser parser;

    @Before
    public void setup() {
        serializer = new Graphviz2DotSerializer(new GraphConfiguration());
        parser     = Rio.createParser(RDFFormat.TURTLE);
        parser.setRDFHandler(new GraphvizHandler(serializer));
    }


    @Test
    public void testInner() throws RDFParseException, IOException, RDFHandlerException {
        parser.parse(this.getClass().getResourceAsStream("/example1.ttl"),"");
        File f = new File("example1.dot");
        f.setWritable(true);
        f.createNewFile();
        PrintWriter out = new PrintWriter("example1.dot");
        out.println(serializer.toDot());
        out.close();
    }

    @Test
    public void testOneMethod() throws RDFParseException, IOException, RDFHandlerException {
        new RDF2Dot("example1.ttl",RDFFormat.TURTLE).toDot("example2.dot");
    }

    @Test
    public void testListMethod() throws RDFParseException, IOException, RDFHandlerException {
        new RDF2Dot("examples",RDFFormat.RDFXML).toDots("dot","xml");
    }

    @Test
    public void testPath() throws RDFParseException, IOException, RDFHandlerException {
        RDF2Dot r = new RDF2Dot("/example1.ttl",RDFFormat.TURTLE);
        Assert.assertEquals(r.targetPath(Paths.get("/users/"), Paths.get("/name")), Paths.get("/name"));
        Assert.assertEquals(r.targetPath(Paths.get("/users/"), Paths.get("name")), Paths.get("/users/name"));
        Assert.assertEquals(r.concatName(Paths.get("/users/name"), Paths.get("foo.xml"),"xml"), Paths.get("/users/name/foo.dot"));
    }
}
