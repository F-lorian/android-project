package modeles.Modele;

/**
 * Created by Axel_2 on 10/11/2015.
 */
public class TypeSignalement {

    protected int id;
    protected String type;

    public TypeSignalement(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TypeSignalement{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
