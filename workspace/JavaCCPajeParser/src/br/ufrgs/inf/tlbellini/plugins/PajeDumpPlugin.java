package br.ufrgs.inf.tlbellini.plugins;

import java.util.ArrayList;

import br.ufrgs.inf.tlbellini.lib.*;



public class PajeDumpPlugin extends PajePlugin {
	
	StringBuilder sb;
	int lines = 0;
	
	
	public PajeDumpPlugin(int maxlines){
		sb = new StringBuilder();
		MAXLINES = maxlines;
	}
	
	@Override
	public void newType(PajeType type){
		
	}
	
	@Override
	public void destroyedContainer(PajeContainer pajeContainer) {
		String parentName;
		if (pajeContainer.getContainer() == null)
			parentName = "0";
		else
			parentName = pajeContainer.getContainer().getName();

		sb.append("Container, ");
		sb.append(parentName);
		sb.append(", ");
		sb.append(pajeContainer.getType().getName());
		sb.append(", ");
		sb.append(pajeContainer.getStartTime());
		sb.append(", ");
		sb.append(pajeContainer.getEndTime());
		sb.append(", ");
		sb.append(pajeContainer.getEndTime() - pajeContainer.getStartTime());
		sb.append(", ");
		sb.append(pajeContainer.getName());
		sb.append("\n");
		checkLines(lines++);
		
		for (ArrayList<PajeEntity> entities : pajeContainer.getEntities()
				.values()) {
			for (PajeEntity ent : entities) {

				switch (ent.getType().getNature()) {
				case VariableType:
					sb.append("Variable, ");
					sb.append(ent.getContainer().getName());
					sb.append(", ");
					sb.append(ent.getType().getName());
					sb.append(", ");
					sb.append(((PajeSingleTimedEntity) ent).getStartTime());
					sb.append(", ");
					sb.append(((PajeDoubleTimedEntity) ent).getEndTime());
					sb.append(", ");
					sb.append(((PajeDoubleTimedEntity) ent).getEndTime() - ((PajeSingleTimedEntity) ent).getStartTime());
					sb.append(", ");
					sb.append(((PajeUserVariable) ent).getValue());
					sb.append("\n");
					checkLines(lines++);
					break;
				case StateType:
					sb.append("State, ");
					sb.append(ent.getContainer().getName());
					sb.append(", ");
					sb.append(ent.getType().getName());
					sb.append(", ");
					sb.append(((PajeSingleTimedEntity) ent).getStartTime());
					sb.append(", ");
					sb.append(((PajeDoubleTimedEntity) ent).getEndTime());
					sb.append(", ");
					sb.append(((PajeDoubleTimedEntity) ent).getEndTime() - ((PajeSingleTimedEntity) ent).getStartTime());
					sb.append(", ");
					sb.append(((PajeUserState) ent).getImbrication());
					sb.append(", ");
					sb.append(((PajeValueEntity) ent).getValue().getName());
					sb.append("\n");
					checkLines(lines++);
					break;
				case LinkType:
					sb.append("Link, ");
					sb.append(ent.getContainer().getName());
					sb.append(", ");
					sb.append(ent.getContainer().getName());
					sb.append(", ");
					sb.append(((PajeSingleTimedEntity) ent).getStartTime());
					sb.append(", ");
					sb.append(((PajeDoubleTimedEntity) ent).getEndTime());
					sb.append(", ");
					sb.append(((PajeDoubleTimedEntity) ent).getEndTime() - ((PajeSingleTimedEntity) ent).getStartTime());
					sb.append(", ");
					sb.append(((PajeValueEntity) ent).getValue().getName());
					sb.append(", ");
					sb.append(((PajeUserLink) ent).getStartContainer().getName());
					sb.append(((PajeUserLink) ent).getEndContainer().getName());
					sb.append("\n");
					checkLines(lines++);
					break;
				default:
					break;

				}
			}
		}
		

	}

	@Override
	public void popState(PajeUserState state){
		sb.append("State, ");
		sb.append(state.getContainer().getName());
		sb.append(", ");
		sb.append(state.getType().getName());
		sb.append(", ");
		sb.append(state.getStartTime());
		sb.append(", ");
		sb.append(state.getEndTime());
		sb.append(", ");
		sb.append(state.getEndTime() - state.getStartTime());
		sb.append(", ");
		sb.append(state.getImbrication());
		sb.append(", ");
		sb.append(state.getValue().getName());
		sb.append("\n");
		checkLines(lines++);
		
	}
	
	@Override
	public void newCompletedLink(PajeUserLink link){
		sb.append("Link, ");
		sb.append(link.getContainer().getName());
		sb.append(", ");
		sb.append(link.getContainer().getName());
		sb.append(", ");
		sb.append(link.getStartTime());
		sb.append(", ");
		sb.append(link.getEndTime());
		sb.append(", ");
		sb.append(link.getEndTime() - link.getStartTime());
		sb.append(", ");
		sb.append(link.getValue().getName());
		sb.append(", ");
		sb.append(link.getStartContainer().getName());
		sb.append(link.getEndContainer().getName());
		sb.append("\n");
		checkLines(lines++);
	}
	
	@Override
	public void updateVar(PajeEntity first, PajeEntity last, PajeUserVariable var) {
		//can't remove from memory because need the last one to calculate
		//value is dumped in destroy container
	}
	
	@Override
	public void newEvent(PajeUserEvent event){
		sb.append("Event, ");
		sb.append(event.getContainer().getName());
		sb.append(", ");
		sb.append(event.getType().getName());
		sb.append(", ");
		sb.append(event.getStartTime());
		sb.append(", ");
		sb.append(event.getValue().getName());
		sb.append("\n");
		checkLines(lines++);
	}
	
	private void checkLines(int lines) {
		if(lines == MAXLINES){
			System.out.println(sb.toString());
			sb.setLength(0);
		}
	}

	@Override
	public void newValue(PajeValue value) {

	}

	@Override
	public void newCreatedContainer(PajeContainer newContainer) {
		
	}

	@Override
	public void setState(PajeUserState newState) {
		
	}

	@Override
	public void pushState(PajeUserState newState) {
		
	}

	@Override
	public void finish() {
		if(sb.length()>0)
			System.out.println(sb);
		
	}

	@Override
	public void setVar(PajeEntity first, PajeEntity last, PajeUserVariable var) {
		
	}

	@Override
	public void startLink(PajeUserLink link) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endLink(PajeUserLink link) {
		// TODO Auto-generated method stub
		
	}

	

}
