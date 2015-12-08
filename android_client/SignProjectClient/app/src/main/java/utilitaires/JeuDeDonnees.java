package utilitaires;

import android.content.Context;

import modeles.Modele.Groupe;
import modeles.Modele.TypeSignalement;
import modeles.Modele.Utilisateur;
import modeles.ModeleBD.GroupeBD;
import modeles.ModeleBD.GroupeUtilisateurBD;
import modeles.ModeleBD.TypeSignalementBD;
import modeles.ModeleBD.UtilisateurBD;

/**
 * Created by Axel_2 on 08/12/2015.
 */
public class JeuDeDonnees {

    public JeuDeDonnees(Context c)
    {
        if (!estDejaPresent(c))
        {
            addUtilisateurs(c);
            addTypeSignalements(c);
            addGroupes(c);
            addGroupeUtilisateurs(c);
        }
    }

    public static void addUtilisateurs(Context c)
    {
        UtilisateurBD var = new UtilisateurBD(c);
        var.open();

        for (int i=1; i<15; i++)
        {
            var.add(new Utilisateur(0,"user"+i,"",null,null,null));
        }

        var.close();
    }

    public static void addTypeSignalements(Context c)
    {
        TypeSignalementBD var = new TypeSignalementBD(c);
        var.open();

        var.add(new TypeSignalement(0, "Controleur"));
        var.add(new TypeSignalement(0,"Horaires"));
        var.add(new TypeSignalement(0, "Accidents / Autres"));

        var.close();
    }

    public static void addGroupes(Context c)
    {
        GroupeBD var = new GroupeBD(c);
        var.open();

        var.add(new Groupe(0, "groupe1", Groupe.typePublic, null, null, new Utilisateur(1, "", "", null, null, null)));
        var.add(new Groupe(0,"groupe2",Groupe.typePublic,null,null,new Utilisateur(2,"","",null,null,null)));
        var.add(new Groupe(0,"groupe3",Groupe.typePrivé,null,null,new Utilisateur(3,"","",null,null,null)));
        var.add(new Groupe(0,"groupe4",Groupe.typePublic,null,null,new Utilisateur(4,"","",null,null,null)));

        var.close();
    }

    public static void addGroupeUtilisateurs(Context c)
    {
        GroupeUtilisateurBD var = new GroupeUtilisateurBD(c);
        var.open();

        var.add(1, 1, GroupeUtilisateurBD.ETAT_APPARTIENT);
        var.add(3, 1, GroupeUtilisateurBD.ETAT_APPARTIENT);
        var.add(4,1,GroupeUtilisateurBD.ETAT_APPARTIENT);
        var.add(5, 1, GroupeUtilisateurBD.ETAT_APPARTIENT);

        var.add(2,2,GroupeUtilisateurBD.ETAT_APPARTIENT);
        var.add(6,1,GroupeUtilisateurBD.ETAT_APPARTIENT);
        var.add(7,2,GroupeUtilisateurBD.ETAT_APPARTIENT);
        var.add(8,1,GroupeUtilisateurBD.ETAT_APPARTIENT);

        var.add(3,3,GroupeUtilisateurBD.ETAT_APPARTIENT);
        var.add(5,3,GroupeUtilisateurBD.ETAT_APPARTIENT);
        var.add(7,3,GroupeUtilisateurBD.ETAT_APPARTIENT);
        var.add(12,3,GroupeUtilisateurBD.ETAT_APPARTIENT);
        var.add(10,3,GroupeUtilisateurBD.ETAT_ATTENTE);
        var.add(11,3,GroupeUtilisateurBD.ETAT_ATTENTE);

        var.add(4,4,GroupeUtilisateurBD.ETAT_APPARTIENT);
        var.add(9,4,GroupeUtilisateurBD.ETAT_ATTENTE);

        var.close();
    }

    public static boolean estDejaPresent(Context c)
    {
        GroupeUtilisateurBD var = new GroupeUtilisateurBD(c);
        TypeSignalementBD var1 = new TypeSignalementBD(c);

        var.open();
        var1.open();

        boolean b = var.getCount() > 0 && var1.getCount() > 0;

        var.close();
        var1.close();

        return b;
    }

}
