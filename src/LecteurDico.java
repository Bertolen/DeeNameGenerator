import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LecteurDico {

	private ArrayList<String> Noms;
	private ArrayList<String> Adjectifs;
	
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
		
		// initialisation de variables
		File file = new File(fileName);
		Noms = new ArrayList<>();
		Adjectifs = new ArrayList<>();
		
		// ouverture du fichier en lecture
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
			
			// TODO Affichier un message pour indiquer que le fichier n'a pas été trouvé
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
		
		switch(type) {
		case "Adj":
			ajoutUnique(Adjectifs, mot);
			if(debug){
				System.out.println("Nouvel adjectif : " + mot);
			}
			break;

		case "Nom":
			ajoutUnique(Noms, mot);
			if(debug){
				System.out.println("Nouveau nom : " + mot);
			}
			break;
			
		default:
			if(debug){
				System.out.println("Mot " + mot + " ignoré, car de type " + type + ".");
			}
			break;
		}
	}
	
	public String suppresSpace(String string) {
		String oldString = new String(string);
		String newString = new String(string.replace("  ", " "));
		
		while(!newString.equals(oldString)) {
			oldString = new String(newString);
			newString = newString.replace("  ", " ");
		}
		
		return newString;
	}
	
	public void ajoutUnique (List<String> liste, String mot) {
		if(!liste.contains(mot)){
			liste.add(mot);
		}
	}
	
	// Getters
	public List<String> noms(){
		return Noms;
	}
	
	public List<String> adjectifs() {
		return Adjectifs;
	}
}
