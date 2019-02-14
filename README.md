# jtranslate
Adds translation capabilities to Java code

Customizable scripting engine, groups, singular, plural or custom groups. An example langfile is given, containing only two groups, singular and plural for languages with theese two forms, but more can be created and customized, allowing more complex counting. Full sentence translation with word change depending on numerical value. See examples below.

For example:
	1 message, 2 messages
	1 day, 2 days
	You have 4 new messages and 1 new mail!
	You have 1 new message and 3 new mails!

Example use:

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
        

Example translation file:

	<lang code="en">
	
	<t key="greeting_hello">Hello!</t>
	<t key="you_have">You have</t>
	
	<t key="new_message">
		<g group="sg">new message</g>
		<g group="pl">new messages</g>
	</t>
	
	<t key="new_mail">
		<g group="sg">new mail</g>
		<g group="pl">new mails</g>
	</t>
	
	<t key="test_sentence_simple" map_nmes="new_message">You have %nmes_val% %nmes%!</t>
	<t key="test_sentence_compound" map_nmes="new_message" map_nmail="new_mail">You have %nmes_val% %nmes% and %nmail_val% %nmail%!</t>
	
	<t key="day">
		<g group="sg">day</g>
		<g group="pl">days</g>
	</t>
	
	<script engine="nashorn">
		<![CDATA[
			
			function countableForm(key, num){
				if(num==1 || num==-1)return lang.getGroupValue(key, "sg");
				else return lang.getGroupValue(key, "pl");
			}
			
		]]>
	</script>
</lang>
