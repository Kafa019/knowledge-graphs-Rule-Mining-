package kg;
import java.util.HashSet;
import java.util.Set;

public class SkinCase {

    private String imageId;

    private String diagnosis;

    private String category3;

    private String category9;

    private String skinType;

    private Set<String> features = new HashSet<>();

    public SkinCase() {
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getCategory3() {
        return category3;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public String getCategory9() {
        return category9;
    }

    public void setCategory9(String category9) {
        this.category9 = category9;
    }

    public String getSkinType() {
        return skinType;
    }

    public void setSkinType(String skinType) {
        this.skinType = skinType;
    }

    public Set<String> getFeatures() {
        return features;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
    }

}