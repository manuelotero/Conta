package com.ts.objects;

public class PruebaPrincipal {
	
	public static void main(String[] args){	
		
		Colaborador colaborador1 = new Colaborador("Laura Quesada B.",111880703);	
		Colaborador colaborador2 = new Colaborador("Esteban Irias Q.",222222222);	
		
		Edificio edificio=new Edificio ("San Ramon");
		edificio.crearListas(1);
		edificio.agregarColaboradorList(colaborador1);
		edificio.agregarColaboradorList(colaborador2);		
		
		System.out.println(" Lista: "+edificio.listColaboradores); 		
		//for (int contador=0; contador < edificio.listColaboradores.size(); contador++)
			
			//System.out.println(edificio.listColaboradores.get(contador));
	}

}