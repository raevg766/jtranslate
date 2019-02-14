package me.raevg766.jtranslate;


import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.script.ScriptEngineManager;

public class TranslMain {

	public static void main(String[] args) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
		
		//can load langfile like this
        //Lang l=new Lang(factory, new FileInputStream("en.langfile"));
        
        //or
        //Lang l=new Lang(factory, TranslMain.class.getResourceAsStream("en.langfile"));
        
        //or
        Lang l=LangLoader.loadLangfileWithCodeFrom(manager, new File("."), "bg");
        if(l==null) {
        	System.out.println("Preferred language not found! Loading default language.");
        	l=LangLoader.loadLangfileWithCodeFrom(manager, new File("."), "en");
        }
        
        
        
        System.out.println(l.getTranslation("greeting_hello"));
        System.out.println("1 "+l.getCountableForm("day", 1));
        System.out.println("2 "+l.getCountableForm("day", 2));
        
        int count=1;
        System.out.println(l.getTranslation("greeting_hello")+" "+l.getTranslation("you_have")+" "+count+" "+l.getCountableForm("new_message", count));
        
        
        System.out.println(l.parseSentence("test_sentence_simple", 2));
        System.out.println(l.parseSentence("test_sentence_simple", 1));
        System.out.println(l.parseSentence("test_sentence_simple", 1.3));
        System.out.println(l.parseSentence("test_sentence_simple", 1.5f));
        
        System.out.println(l.parseSentence("test_sentence_compound", new Object[] {"nmes", 2.3f, "nmail", 1}));
        System.out.println(l.parseSentence("test_sentence_compound", new Object[] {"nmes", 1, "nmail", 2.3}));
        
        
        
        System.out.println("Loading .langfile ...");
        List<Lang> langs=LangLoader.loadLangfilesFrom(manager, new File("."));
        for(Lang lang1:langs) {
        	System.out.println("Loaded language with code: "+lang1.getCode());
        }
	}

}
