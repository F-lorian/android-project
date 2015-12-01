package utilitaires;

/**
 * Created by Axel_2 on 30/11/2015.
 */
public class ItemNavigationDrawer {

    String ItemName;
    int imgResID;
    String title;

    public ItemNavigationDrawer(String itemName, int imgResID) {
        ItemName = itemName;
        this.imgResID = imgResID;
        this.title = null;
    }

    public ItemNavigationDrawer(String titre) {
        this.ItemName = null;
        this.imgResID = 0;
        this.title = titre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItemName() {
        return ItemName;
    }
    public void setItemName(String itemName) {
        ItemName = itemName;
    }
    public int getImgResID() {
        return imgResID;
    }
    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

}