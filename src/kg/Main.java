package kg;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {

            String fitzpatrick =
                    "fitzpatrick17k.csv";

            String fitzpatrickSkincon =
                    "fitzpatrick17k_skincon.csv";

            String ddiSkincon =
                    "ddi_skincon.csv";

            System.out.println("==================================");
            System.out.println("KNOWLEDGE GRAPH GENERATOR");
            System.out.println("==================================");

            DataLoader loader =
                    new DataLoader();

            List<SkinCase> cases =
                    loader.load(
                            fitzpatrick,
                            fitzpatrickSkincon,
                            ddiSkincon);

            System.out.println("Immagini lette: "
                    + cases.size());

            KnowledgeGraphBuilder builder =
                    new KnowledgeGraphBuilder();

            List<Triple> triples =
                    builder.build(cases);

            System.out.println("Triple iniziali: "
                    + triples.size());

            RelationMiner miner =
                    new RelationMiner();

            miner.mine(cases, triples);

            System.out.println("Triple dopo il mining: "
                    + triples.size());

            DatasetSplitter splitter =
                    new DatasetSplitter();

            splitter.split(triples);

            Statistics statistics =
                    new Statistics();

            statistics.generate(
                    cases,
                    triples);

            FileWriterUtil writer =
                    new FileWriterUtil();

            writer.writeTriples(
                    triples,
                    "knowledge_graph.txt");

            System.out.println();
            System.out.println("==================================");
            System.out.println("OPERAZIONE COMPLETATA");
            System.out.println("==================================");

        }
        catch (Exception e) {

            e.printStackTrace();

        }

    }

}