package utilitaires;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import modeles.Modele.Arret;
import modeles.Modele.Ligne;
import modeles.ModeleBD.ArretBD;
import modeles.ModeleBD.LigneArretBD;
import modeles.ModeleBD.LigneBD;

/**
 * Created by Axel_2 on 26/11/2015.
 */
public class ParserKMLToBD {

    private static final String ns = null;

    public ArrayList<Ligne> lignes;
    public ArrayList<Arret> arrets;

    public ParserKMLToBD()
    {
        this.lignes = new ArrayList<Ligne>();
        this.arrets = new ArrayList<Arret>();
    }

    public void parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            parser.nextTag();
            this.readDocument(parser);
        } finally {
            in.close();
        }
    }

    private void readDocument(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Document");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Folder")) {
                this.readFolder(parser);
            } else {
                this.skip(parser);
            }
        }
    }

    private void readFolder(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Folder");
        String nameFolder = "";
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("name")) {
                nameFolder = this.readText(parser);
            }
            else if (name.equals("Placemark"))
            {
                if (nameFolder.equals("Lignes"))
                {
                    this.readLigne(parser);
                }
                else
                {
                    this.readArret(parser);
                }
            }
            else
            {
                this.skip(parser);
            }
        }
    }

    private void readLigne(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Placemark");

        String nom = null;
        String coordonnees = null;
        String description = null;

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("name"))
            {
                parser.require(XmlPullParser.START_TAG, ns, "name");
                nom = this.readText(parser);
                parser.require(XmlPullParser.END_TAG, ns, "name");
            }
            else if (name.equals("description"))
            {
                parser.require(XmlPullParser.START_TAG, ns, "description");
                description = this.readText(parser);
                parser.require(XmlPullParser.END_TAG, ns, "description");
            }
            else if (name.equals("LineString"))
            {
                parser.require(XmlPullParser.START_TAG, ns, "LineString");
                coordonnees = this.readLineString(parser);
                parser.require(XmlPullParser.END_TAG, ns, "LineString");
            }
            else
            {
                this.skip(parser);
            }
        }

        this.lignes.add(new Ligne(0, nom, coordonnees, description, null));
    }

    private String readLineString(XmlPullParser parser)throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "LineString");
        String coordonnes = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("coordinates")) {
                coordonnes = this.readText(parser);
            }
            else
            {
                this.skip(parser);
            }
        }

        return coordonnes;
    }

    private void readArret(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Placemark");

        String nom = null;
        String coordonnees = null;
        String direction = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("name"))
            {
                parser.require(XmlPullParser.START_TAG, ns, "name");
                nom = this.readText(parser);
                parser.require(XmlPullParser.END_TAG, ns, "name");
            }
            else if (name.equals("description"))
            {
                parser.require(XmlPullParser.START_TAG, ns, "description");
                direction = this.readText(parser);
                parser.require(XmlPullParser.END_TAG, ns, "description");
            }
            else if (name.equals("Point"))
            {
                parser.require(XmlPullParser.START_TAG, ns, "Point");
                coordonnees = this.readPoint(parser);
                parser.require(XmlPullParser.END_TAG, ns, "Point");
            }
            else
            {
                this.skip(parser);
            }
        }

        this.arrets.add(new Arret(0, nom, coordonnees, direction, null,null));

    }

    private String readPoint(XmlPullParser parser)throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Point");
        String coordonnes = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("coordinates")) {
                coordonnes = this.readText(parser);
            }
            else
            {
                this.skip(parser);
            }
        }

        return coordonnes;
    }


    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public boolean addInBD(Context c)
    {
        return this.addLigneBD(c) && this.addArretBD(c) && this.addLigneArretBD(c);
    }

    private boolean addLigneBD(Context c)
    {
        LigneBD ligneBd = new LigneBD(c);
        ligneBd.open();

        for (int i = 0; i<this.lignes.size(); i++) {

            int id = (int) ligneBd.add(this.lignes.get(i));

            if (id < 0) {
                ligneBd.close();
                return false;
            }

            this.lignes.get(i).setId(id);
        }

        ligneBd.close();
        return true;
    }

    private boolean addArretBD(Context c)
    {
        ArretBD arretBd = new ArretBD(c);
        arretBd.open();

        for (int i = 0; i<this.arrets.size(); i++) {

            int id = (int) arretBd.add(this.arrets.get(i));

            if (id < 0) {
                arretBd.close();
                return false;
            }

            this.arrets.get(i).setId(id);
        }

        arretBd.close();
        return true;
    }

    private boolean addLigneArretBD(Context c)
    {
        LigneArretBD labd = new LigneArretBD(c);

        labd.open();

        for (int i=0; i<this.lignes.size(); i++)
        {
            String description = this.lignes.get(i).getDescription();
            String[] destinations = description.split(" - ");
            //for (int k=0; k<destinations.length; k++) {
                //System.out.println(destinations[k] + " -- " + this.lignes.get(i).getNom());
            //}
            for (int j=0; j<this.arrets.size(); j++)
            {
                String direction = this.arrets.get(j).getDirection();
                for (int k=0; k<destinations.length; k++)
                {
                    boolean appartientALigne = false;

                    if (destinations[k].contains(" / "))
                    {
                        String dest = destinations[k].replace(" / ","_");

                        if (direction.contains(dest))
                        {
                            appartientALigne = true;
                        }
                        else
                        {
                            String[] dests = dest.split("_");

                            for (int ind = 0; ind<dests.length; ind++)
                            {
                                if (dests[ind].equals(direction))
                                {
                                    appartientALigne = true;
                                }
                            }
                        }
                    }
                    else
                    {
                        appartientALigne = direction.contains(destinations[k]);
                    }

                    if (appartientALigne)
                    {
                        //System.out.println(this.lignes.get(i).getNom() + " ** " + this.arrets.get(j).getNom() + "  " + destinations[k]);
                        if(labd.add(this.lignes.get(i).getId(),this.arrets.get(j).getId()) < 0)
                        {
                            labd.close();
                            return false;
                        }
                    }
                }
            }
        }

        labd.close();
        return true;
    }
}
