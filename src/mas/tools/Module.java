package mas.tools;

import java.util.ArrayList;
import java.util.List;

public class Module {
	
	private List<Functor> listAction;
	private List<Functor> listCondition;
	private int defaultSignal;
	
	public Module(){
		this.defaultSignal = -1;
		this.listAction = new ArrayList<Functor>();
		this.listCondition= new ArrayList<Functor>();
		
	}
	
	public void act(){
		for (Functor f : this.listAction){
			f.act(this);
		}
	}

	public int output(){
		int signal = 0;
		for (Functor f : this.listCondition){
			if (f.run(this)){
				return signal;
			}
			signal++;
		}
		return this.defaultSignal;
	}
}
