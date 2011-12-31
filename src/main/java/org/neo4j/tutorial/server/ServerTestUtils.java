package org.neo4j.tutorial.server;

import org.neo4j.kernel.AbstractGraphDatabase;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.server.database.GraphDatabaseFactory;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class ServerTestUtils
{
    public static final GraphDatabaseFactory EMBEDDED_GRAPH_DATABASE_FACTORY = new GraphDatabaseFactory()
    {
        public AbstractGraphDatabase createDatabase(String databaseStoreDirectory,
                                                    Map<String, String> databaseProperties)
        {
            return new EmbeddedGraphDatabase(databaseStoreDirectory, databaseProperties);
        }
    };

    public static File createTempDir(String prefix, String suffix) throws IOException
    {
        File d = File.createTempFile(prefix, suffix);
        if (!d.delete())
        {
            throw new RuntimeException("temp config directory pre-delete failed");
        }
        if (!d.mkdirs())
        {
            throw new RuntimeException("temp config directory not created");
        }
        return d;
    }

    public static File createTempDir() throws IOException
    {
        return createTempDir("neo4j-test", "dir");
    }

    public static File createTempPropertyFile() throws IOException
    {
        return createTempPropertyFile(createTempDir());
    }

    public static void writePropertiesToFile(String outerPropertyName, Map<String, String> properties,
                                             File propertyFile)
    {
        writePropertyToFile(outerPropertyName, asOneLine(properties), propertyFile);
    }

    private static String asOneLine(Map<String, String> properties)
    {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> property : properties.entrySet())
        {
            builder.append((builder.length() > 0 ? "," : ""));
            builder.append(property.getKey());
            builder.append("=");
            builder.append(property.getValue());
        }
        return builder.toString();
    }

    public static void writePropertyToFile(String name, String value, File propertyFile)
    {
        Properties properties = loadProperties(propertyFile);
        properties.setProperty(name, value);
        storeProperties(propertyFile, properties);
    }

    private static void storeProperties(File propertyFile, Properties properties)
    {
        OutputStream out = null;
        try
        {
            out = new FileOutputStream(propertyFile);
            properties.store(out, "");
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        } finally
        {
            safeClose(out);
        }
    }

    private static Properties loadProperties(File propertyFile)
    {
        Properties properties = new Properties();
        if (propertyFile.exists())
        {
            InputStream in = null;
            try
            {
                in = new FileInputStream(propertyFile);
                properties.load(in);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            } finally
            {
                safeClose(in);
            }
        }
        return properties;
    }

    private static void safeClose(Closeable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static File createTempPropertyFile(File parentDir) throws IOException
    {
        return new File(parentDir, "test-" + new Random().nextInt() + ".properties");
    }
}
