package com.yyHaker.semantic.symbols;

import com.yyHaker.semantic.inter.Id;
import com.yyHaker.semantic.inter.Node;
import  com.yyHaker.semantic.lexer.*;
import com.yyHaker.semantic.parser.Parser;
import com.yyHaker.semantic.properties.SymbolProperty;
import com.yyHaker.syntax.property.SyntaxErrorProperty;

import java.util.Hashtable;

public class Env {

	private Hashtable table;
	protected Env prev;



	public Env(Env n) {
		table = new Hashtable();
		prev = n;
	}

	public void put(Token w, Id i) {
		Word word=i.getWord();
		 if (get(w)!=null){
			 Node.semanticErrorList.add(new SyntaxErrorProperty(Lexer.line,word.lexeme,"变量"+word.lexeme+"被重复声明"));
		 }
		table.put(w, i);
		//添加符号表信息
		Node.symbolPropertyList.add(new SymbolProperty(word.lexeme,i.type.lexeme,Parser.used));
	}

	public Id get(Token w) {
		for( Env e = this; e != null; e = e.prev ) {
			Id found = (Id)(e.table.get(w));
			if( found != null ) return found;
		}
		return null;
	}

	/*//获得本对象之前的所有表的符号
	public List<String> getAllID(){
		List<String> stringList=new ArrayList<>();
		 for (Env e=this;e!=null;e=e.prev){
			  for (Object key:e.table.keySet()){
				   Id id =(Id) e.table.get(key);
				  Word word=id.getWord();
				   //标识符的名字    类型     所在块
				   stringList.add(word.lexeme+" "+key.toString()+" "+e.toString()+""+ Parser.used);
			  }
		 }
		 return stringList;
	}*/

}
