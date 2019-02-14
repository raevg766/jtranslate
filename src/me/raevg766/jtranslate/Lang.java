package me.raevg766.jtranslate;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Lang {
	
	private String code;
	private Map<String, TranslGroup> translations=new HashMap<>();
	private ScriptEngine engine;
	private String script;
	
	/**
	 * Constructs a new Lang object
	 * <p>
	 * Made for use by the java code!
	 * </p>
	 * @param manager The ScriptEngineManager object
	 * @param inputStream The InputStream object of the .langfile file
	 * @author raevg766
	 */
	public Lang(ScriptEngineManager manager, InputStream inputStream) throws ParserConfigurationException, SAXException, IOException, ScriptException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputStream);
		doc.getDocumentElement().normalize();
		
		this.code=doc.getDocumentElement().getAttribute("code");
		
		NodeList nList = doc.getElementsByTagName("t");
		
		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				
				Element eElement = (Element) nNode;
			
				String key=eElement.getAttribute("key");
				TranslGroup g=new TranslGroup();
				g.value=eElement.getTextContent();
				
				NamedNodeMap attributes=eElement.getAttributes();
				for(int k=0;k<attributes.getLength();k++) {
					if(attributes.item(k).getNodeName().startsWith("map_")) {
						g.mapped_translations.put(attributes.item(k).getNodeName().replace("map_", ""), eElement.getAttribute(attributes.item(k).getNodeName()));
					}
				}
				
				NodeList groupList=eElement.getElementsByTagName("g");
				for(int k=0;k<groupList.getLength();k++) {
					Element el1=(Element) groupList.item(k);
					g.values.put(el1.getAttribute("group"), el1.getTextContent());
				}

				translations.put(key, g);
			}
		}
		
		engine=manager.getEngineByName(((Element)doc.getElementsByTagName("script").item(0)).getAttribute("engine"));
		script=doc.getElementsByTagName("script").item(0).getTextContent();
		
		Bindings binds=engine.getBindings(ScriptContext.ENGINE_SCOPE);
		binds.put("lang", this);
		engine.eval(script);
	}
	
	/**
	 * Gets the code of the language
	 * <p>Made for use by the java code!</p>
	 * @return The code of the language
	 * @author raevg766
	 */
	public String getCode() {
		return code;
	}
	
	
	/**
	 * Gets the static translation associated with the specified key
	 * <p>Made for use by the java code!</p>
	 * @param key The key to search for
	 * @return The static translation of the specified key
	 * @author raevg766
	 */
	public String getTranslation(String key) {
		if(!translations.containsKey(key))return key;
		return translations.get(key).value;
	}
	
	
	/**
	 * Gets the countable form of the word associated with the specified key
	 * <p>Made for use by the java code!</p>
	 * @param key The key to search for
	 * @param num The number
	 * @return The countable form of the word of the specified key
	 * @author raevg766
	 */
	public String getCountableForm(String key, double num) {
		Invocable invocable = (Invocable) engine;
		
		try {
			Object result = invocable.invokeFunction("countableForm", key, num);
			return result.toString();
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
			return key;
		}
	}
	
	
	/**
	 * Parses a full sentence associated with the specified key
	 * <p>Made for use by the java code!</p>
	 * @param key The key to search for
	 * @param num The number
	 * @return A parsed sentence, all mapped occurrences have the same num count, but are changed according to the num value, just like passing getCountableForm
	 * @author raevg766
	 */
	public String parseSentence(String key, Number num) {
		if(!translations.containsKey(key))return key;
		String sentence=new String(translations.get(key).value);
		for(Entry<String, String> e:translations.get(key).mapped_translations.entrySet()) {
			if(num instanceof Double) {
				sentence=sentence.replaceAll("%"+e.getKey()+"%", getCountableForm(e.getValue(), (double)num));
				sentence=sentence.replaceAll("%"+e.getKey()+"_val%", (double)num+"");
			}else if(num instanceof Float) {
				sentence=sentence.replaceAll("%"+e.getKey()+"%", getCountableForm(e.getValue(), (float)num));
				sentence=sentence.replaceAll("%"+e.getKey()+"_val%", (float)num+"");
			}else {
				sentence=sentence.replaceAll("%"+e.getKey()+"%", getCountableForm(e.getValue(), (int)num));
				sentence=sentence.replaceAll("%"+e.getKey()+"_val%", (int)num+"");
			}
		}
		return sentence;
	}
	
	
	/**
	 * Parses a full sentence associated with the specified keys-values
	 * <p>Made for use by the java code!</p>
	 * @param key The key to search for
	 * @param values Values like this: new Object[] {"mappedKey", mappedKeyValueInt, "mappedKey2", mappedKey2ValueDouble}
	 * @return A parsed sentence, according to the passed values. If you haven't supplied all the occurrences in the values attribute, they will remain unchanged in the returned sentence!
	 * @author raevg766
	 */
	public String parseSentence(String key, Object[] values) {
		if(!translations.containsKey(key))return key;
		TranslGroup trg=translations.get(key);
		String sentence=new String(trg.value);
		for(int i=0;i<values.length;i+=2) {
			if(values[i+1] instanceof Double) {
				sentence=sentence.replaceAll("%"+values[i]+"%", getCountableForm(trg.mapped_translations.get(values[i]), (double)values[i+1]));
				sentence=sentence.replaceAll("%"+values[i]+"_val%", (double)values[i+1]+"");
			}else if(values[i+1] instanceof Float) {
				sentence=sentence.replaceAll("%"+values[i]+"%", getCountableForm(trg.mapped_translations.get(values[i]), (float)values[i+1]));
				sentence=sentence.replaceAll("%"+values[i]+"_val%", (float)values[i+1]+"");
			}else {
				sentence=sentence.replaceAll("%"+values[i]+"%", getCountableForm(trg.mapped_translations.get(values[i]), (int)values[i+1]));
				sentence=sentence.replaceAll("%"+values[i]+"_val%", (int)values[i+1]+"");
			}
		}
		return sentence;
	}
	
	
	
	
	//prefferably for langfiles
	
	/**
	 * Gets the group value associated with the specified key
	 * <p>Made for use by langfile scripts!</p>
	 * @param key The key to search for
	 * @param group The group name to search for
	 * @return The group value of the specified key
	 * @author raevg766
	 */
	public String getGroupValue(String key, String group) {
		if(!translations.containsKey(key))return key;
		return translations.get(key).values.getOrDefault(group, key);
	}
	
}


class TranslGroup{
	
	public String value;
	public Map<String, String> values=new HashMap<>();
	public Map<String, String> mapped_translations=new HashMap<>();
	
	public TranslGroup() {
		
	}
	
}
