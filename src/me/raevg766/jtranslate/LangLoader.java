package me.raevg766.jtranslate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class LangLoader {
	
	
	public static List<Lang> loadLangfilesFrom(ScriptEngineManager manager, File dir) {
		List<Lang> list=new ArrayList<>();
		
		if(!dir.isDirectory())return list;
		
		for(File f:dir.listFiles()) {
			if(f.getName().endsWith(".langfile")) {
				try {
					Lang l=new Lang(manager, new FileInputStream(f));
					list.add(l);
				} catch (ParserConfigurationException | SAXException | IOException | ScriptException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	public static Lang loadLangfileWithCodeFrom(ScriptEngineManager manager, File dir, String code) {
		if(!dir.isDirectory())return null;
		
		for(File f:dir.listFiles()) {
			if(f.getName().endsWith(".langfile")) {
				try {
					Lang l=new Lang(manager, new FileInputStream(f));
					if(l.getCode().equals(code))return l;
				} catch (ParserConfigurationException | SAXException | IOException | ScriptException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	
	
}
