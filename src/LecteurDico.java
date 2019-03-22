import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LecteurDico {

	private HashMap<String, ArrayList<String>> dictionnaire;
	private Random r;
	
	private boolean debug = false;
	
	// Constructeur sans arguments, il prendra la valeur par défaut du fichier
	public LecteurDico() {
		this("");
	}

	// Constructeur avec le nom du fichier en argument
	public LecteurDico (String fileName) {
		
		// Valeur par défaut du nom de fichier
		if (fileName == null || fileName.equals("")) {
			fileName = "DicoFR.txt";
		}

		// init des proprétés de la classe
		dictionnaire = new HashMap<>();
		r = new Random();

		// ouverture du fichier en lecture
		File file = new File(fileName);
		
		try(
			// Lecture du dictionnare 
			FileInputStream fis = new FileInputStream(file);
			
			// utilisation d'un BufferedReader pour amélioration des performances 
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			) {
			
			// lecture de chaque ligne
			String newLine = reader.readLine();
			while (newLine != null) {
				lectureLigneFormatee(newLine);
				newLine = reader.readLine();
				
				if (debug) {
					System.out.println("----------------");
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
	}
	
	// Lecture d'une ligne du dictionnaire
	private void lectureLigneFormatee (String ligne) {
		String[] elements = suppresSpace(ligne.replace("\t"," ")).split(" ");
		String mot = elements[0];
		String racine = elements[1];
		String informations = elements[2];
		elements = informations.split(":");
		String type = elements[0];
		
		if(debug) {
			System.out.println("mot : " + mot);
			System.out.println("racine : " + racine);
			System.out.println("infos : " + informations);
			System.out.println("type : " + type);
		}
		
		ArrayList<String> liste = dictionnaire.get(type);
		
		// si le dictionnaire ne connais pas ce type de mot alors on l'ajoute
		if(liste == null){
			liste = new ArrayList<>();
			dictionnaire.put(type, liste);
			if (debug) {
				System.out.println("Nouveau type de mot : " + type);
			}
		}
		
		switch(type) {
		
		case "Ver":
			// si c'est un verbe on cherche à savoir si c'est la conjugaison de l'impératif présent
			boolean imperatifPresent = false;
			for(String conjugaison : elements){
				if(conjugaison.equals("ImPre+SG+P2")){
					imperatifPresent = true;
				}
			}
			
			// si c'est en effet l'impératif présent alors on l'ajoute à la liste
			if(imperatifPresent){
				ajoutUnique(liste, mot);
				
				if(debug){
					System.out.println("Nouveau verbe : " + mot + ".");
				}
			} else if (debug) {
				System.out.println("Conjugaison '" + mot + "' du verbe '" + racine + "' ignorée car ce n'est pas l'impératif présent.");
			}
			break;
			
		default:
			ajoutUnique(liste, racine);
			if(debug){
				System.out.println("Nouveau mot de type " + type + " : " + racine);
			}
			break;
		} 
	}
	
	// restitue un mot aléatoire du type donné
	public String motAleatoire(String type) {
		ArrayList<String> liste = dictionnaire.get(type);
		
		if (liste == null) {
			return null;
		}
		
		return liste.get( r.nextInt( liste.size()));
	}
	
	public String combinaisonAleatoire(){
		String text = "";
		
		if(r.nextBoolean()){
			text = text.concat(motAleatoire("Nom") + " " + motAleatoire("Adj"));
		} else {
			text = text.concat(motAleatoire("Ver") + " " + motAleatoire("Nom"));
		}
		
		return text;
	}
	
	// retire tous les double espaces d'une chaîne de caractères
	public String suppresSpace(String string) {
		String oldString = string;
		String newString = string.replace("  ", " ");
		
		while(!newString.equals(oldString)) {
			oldString = newString;
			newString = newString.replace("  ", " ");
		}
		
		return newString;
	}
	
	// on ajoute un element à une liste en s'assurant que c'est la seule occurrence de cet element
	public void ajoutUnique (List<String> liste, String mot) {
		if(!liste.contains(mot)){
			liste.add(mot);
		}
	}
}
