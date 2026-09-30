package kg;
import java.io.*;
import java.util.*;

public class DataLoader {

    private final Map<String, SkinCase> cases = new HashMap<>();

    public List<SkinCase> load(String fitzpatrickFile,
                               String fitzpatrickSkinconFile,
                               String ddiSkinconFile) throws IOException {

        loadMainDataset(fitzpatrickFile);

        loadSkinconFeatures(fitzpatrickSkinconFile);

        loadSkinconFeatures(ddiSkinconFile);

        return new ArrayList<>(cases.values());
    }

    private void loadMainDataset(String file) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(file));

        String header = br.readLine();

        if (header == null) {
            br.close();
            return;
        }

        String[] columns = header.split(",");

        Map<String,Integer> index = new HashMap<>();

        for(int i=0;i<columns.length;i++){

            index.put(columns[i].trim(),i);

        }

        String line;

        while((line=br.readLine())!=null){

            String[] values = splitCSV(line);

            SkinCase sc = new SkinCase();

            sc.setImageId(values[index.get("md5hash")]);

            sc.setDiagnosis(values[index.get("label")]);

            sc.setCategory3(values[index.get("three_partition_label")]);

            sc.setCategory9(values[index.get("nine_partition_label")]);

            sc.setSkinType(values[index.get("fitzpatrick_scale")]);

            cases.put(sc.getImageId(),sc);

        }

        br.close();

    }

    private void loadSkinconFeatures(String file) throws IOException{

        BufferedReader br = new BufferedReader(new FileReader(file));

        String header = br.readLine();

        if(header==null){

            br.close();

            return;

        }

        String[] columns = header.split(",");

        List<String> featureNames = new ArrayList<>();

        for(int i=1;i<columns.length;i++){

            featureNames.add(columns[i]);

        }

        String line;

        while((line=br.readLine())!=null){

            String[] values = splitCSV(line);

            String imageId = values[0];

            SkinCase sc = cases.get(imageId);

            if(sc==null)
                continue;

            for(int i=1;i<values.length;i++){

                String value = values[i].trim();

                if(value.equals("1") ||
                   value.equalsIgnoreCase("true") ||
                   value.equalsIgnoreCase("yes")){

                    sc.getFeatures().add(featureNames.get(i-1));

                }

            }

        }

        br.close();

    }

    private String[] splitCSV(String line){

        List<String> tokens = new ArrayList<>();

        boolean quote=false;

        StringBuilder sb = new StringBuilder();

        for(char c: line.toCharArray()){

            if(c=='"'){

                quote=!quote;

            }
            else if(c==',' && !quote){

                tokens.add(sb.toString());

                sb.setLength(0);

            }
            else{

                sb.append(c);

            }

        }

        tokens.add(sb.toString());

        return tokens.toArray(new String[0]);

    }

}