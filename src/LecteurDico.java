import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LecteurDico {

	private ArrayList<String> Noms;
	private ArrayList<String> Adjectifs;
	private ArrayList<String> Verbes;
	private Random r;
	
	private boolean debug = false;
	
	// Constructeur sans arguments, il prendra la valeur par d�faut du fichier
	public LecteurDico() {
		this("");
	}

	// Constructeur avec le nom du fichier en argument
	public LecteurDico (String fileName) {
		
		// Valeur par d�faut du nom de fichier
		if (fileName == null || fileName.equals("")) {
			fileName = "DicoFR.txt";
		}

		// init des propr�t�s de la classe
		Noms = new ArrayList<>();
		Adjectifs = new ArrayList<>();
		Verbes = new ArrayList<>();
		r = new Random();

		// ouverture du fichier en lecture
		File file = new File(fileName);
		
		try(
				// Lecture du dictionnare 
				FileInputStream fis = new FileInputStream(file);
				
				// utilisation d'un BufferedReader pour am�lioration des performances 
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
		
		switch(type) {
		case "Adj":
			ajoutUnique(Adjectifs, racine);
			if(debug){
				System.out.println("Nouvel adjectif : " + racine);
			}
			break;

		case "Nom":
			ajoutUnique(Noms, racine);
			if(debug){
				System.out.println("Nouveau nom : " + racine);
			}
			break;

		case "Ver":
			// si c'est un verbe on cherche � savoir si c'est la conjugaison de l'imp�ratif pr�sent
			boolean imperatifPresent = false;
			for(String conjugaison : elements){
				if(conjugaison.equals("ImPre+SG+P2")){
					imperatifPresent = true;
				}
			}
			
			// si c'est en effet l'imp�ratif pr�sent alors on l'ajoute � la liste
			if(imperatifPresent){
				ajoutUnique(Verbes, mot);
				
				if(debug){
					System.out.println("Nouveau verbe : " + mot);
				}
			} else if (debug) {
				System.out.println("Conjugaison '" + mot + "' du verbe '" + racine + "' ignor�e car ce n'est pas l'imp�ratif pr�sent");
			}
			break;
			
		default:
			if(debug){
				System.out.println("Mot " + racine + " ignor�, car de type " + type + ".");
			}
			break;
		} 
	}
	
	// restitue un nom al�atoire
	public String nomAleatoire() {
		return Noms.get( r.nextInt( Noms.size()));
	}
	
	// restitue un adjectif al�atoire
	public String adjectifAleatoire() {
		return Adjectifs.get( r.nextInt( Adjectifs.size()));
	}
	
	// restitue un verbe al�atoire
	public String verbeAleatoire() {
		return Verbes.get( r.nextInt( Verbes.size()));
	}
	
	public String combinaisonAleatoire(){
		String text = "";
		
		if(r.nextBoolean()){
			text = text.concat(nomAleatoire() + " " + adjectifAleatoire());
		} else {
			text = text.concat(verbeAleatoire() + " " + nomAleatoire());
		}
		
		
		return text;
	}
	
	// retire tous les double espaces d'une cha�ne de caract�res
	public String suppresSpace(String string) {
		String oldString = string;
		String newString = string.replace("  ", " ");
		
		while(!newString.equals(oldString)) {
			oldString = newString;
			newString = newString.replace("  ", " ");
		}
		
		return newString;
	}
	
	// on ajoute un element � une liste en s'assurant que c'est la seule occurrence de cet element
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
	
	public List<String> verbes() {
		return Verbes;
	}
}
