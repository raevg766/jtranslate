# jtranslate
Adds translation capabilities to Java code

Example use:

	ScriptEngineManager factory = new ScriptEngineManager();
		
	//can load langfile like this
        Lang l=new Lang(factory, new FileInputStream("bg.langfile"));
        
        //or
        //Lang l=new Lang(factory, TranslMain.class.getResourceAsStream("bg.langfile"));
        
        
        
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
