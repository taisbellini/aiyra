package br.ufrgs.inf.tlbellini.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class PajeSimulator extends PajeComponent {

	private PajeContainerType rootType;
	private PajeContainer root;

	// map alias with type object
	private Map<String, PajeType> typeMap = new HashMap<String, PajeType>();
	private Map<String, PajeType> typeNamesMap = new HashMap<String, PajeType>();;
	private Map<String, PajeContainer> contMap = new HashMap<String, PajeContainer>();
	private Map<String, PajeContainer> contNamesMap = new HashMap<String, PajeContainer>();

	// private double stopSimulationAtTime;


	protected double lastKnownTime;

	/*
	 * public PajeSimulator(){ this.stopSimulationAtTime = -1; }
	 * 
	 * public PajeSimulator(double stopAt){ this.stopSimulationAtTime = stopAt;
	 * }
	 */

	public void init() {
		// name, alias, parent
		this.rootType = new PajeContainerType("0", "0", null);

		// time, name, alias, parent, type, event
		this.root = new PajeContainer(0.0, "0", "0", null, rootType, null);
		typeMap.put(rootType.getId(), rootType);
		typeNamesMap.put(rootType.getName(), rootType);
		contMap.put(root.getId(), root);
		contNamesMap.put(root.getName(), root);
	}

	protected void setLastKnownTime(PajeTraceEvent event) {
		String time = event.valueForField(PajeFieldName.Time);
		if (time.isEmpty()) {
			this.lastKnownTime = -1;
		} else {
			this.lastKnownTime = Double.parseDouble(time);
		}
	}

	PajeColor getColor(String color, PajeTraceEvent event) throws Exception {
		PajeColor ret = null;
		Pattern patAspas = Pattern.compile("\"");
		String str[] = patAspas.split(color);
		Pattern pat = Pattern.compile(" ");
		String v[] = pat.split(str[1]);
		int line = event.getLine();

		if (!color.isEmpty()) {
			if (v.length == 3) {
				ret = new PajeColor(Float.parseFloat(v[0]),
						Float.parseFloat(v[1]), Float.parseFloat(v[2]), 1);
			} else if (v.length == 4) {
				ret = new PajeColor(Float.parseFloat(v[0]),
						Float.parseFloat(v[1]), Float.parseFloat(v[2]),
						Float.parseFloat(v[3]));
			} else {
				throw new Exception(
						"Could not understand color parameter in line " + line);
			}
		}
		return ret;
	}
	
	public void finish() throws Exception{
		this.root.recursiveDestroy(this.lastKnownTime);
	}

	public void report() {

		for (PajeContainer container : this.contMap.values()) {

			String parentName;
			if (container.getContainer() == null)
				parentName = "0";
			else
				parentName = container.getContainer().getName();

			System.out.println("Container, " + parentName + ", "
					+ container.getType().getName() + ", "
					+ container.getStartTime() + ", " + container.getEndTime()
					+ ", "
					+ (container.getEndTime() - container.getStartTime())
					+ ", " + container.getName());
			
			for (ArrayList<PajeEntity> entities : container.getEntities()
					.values()) {
				for (PajeEntity ent : entities) {

					switch (ent.getType().getNature()) {
					case VariableType:
						System.out
								.println("Variable, "
										+ ent.getContainer().getName()
										+ ", "
										+ ent.getType().getName()
										+ ", "
										+ ((PajeSingleTimedEntity) ent)
												.getStartTime()
										+ ", "
										+ ((PajeSingleTimedEntity) ent)
												.getEndTime()
										+ ", "
										+ (((PajeDoubleTimedEntity) ent)
												.getEndTime() - ((PajeSingleTimedEntity) ent)
												.getStartTime()) + ", "
										+ ((PajeUserVariable) ent).getValue() );
						break;
					case StateType:
						System.out
								.println("State, "
										+ ent.getContainer().getName()
										+ ", "
										+ ent.getType().getName()
										+ ", "
										+ ((PajeSingleTimedEntity) ent)
												.getStartTime()
										+ ", "
										+ ((PajeSingleTimedEntity) ent)
												.getEndTime()
										+ ", "
										+ (((PajeDoubleTimedEntity) ent)
												.getEndTime() - ((PajeSingleTimedEntity) ent)
												.getStartTime())
										+ ", "
										+ ((PajeUserState) ent)
												.getImbrication() + ", "
										+ ((PajeValueEntity) ent).getValue().getName()
												);
						break;
					case EventType:
						System.out.println("Event, "
								+ ent.getContainer().getName() + ", "
								+ ent.getType().getName() + ", "
								+ ((PajeSingleTimedEntity) ent).getStartTime()
								+ ", " + ((PajeUserEvent) ent).getValue().getName());
						break;
					case LinkType:
						System.out
								.println("Link, "
										+ ent.getContainer().getName()
										+ ", "
										+ ent.getType().getName()
										+ ", "
										+ ((PajeSingleTimedEntity) ent)
												.getStartTime()
										+ ", "
										+ ((PajeSingleTimedEntity) ent)
												.getEndTime()
										+ ", "
										+ (((PajeDoubleTimedEntity) ent)
												.getEndTime() - ((PajeSingleTimedEntity) ent)
												.getStartTime())
										+ ", "
										+ ((PajeValueEntity) ent).getValue().getName()
										+ ", "
										+ ((PajeUserLink) ent)
												.getStartContainer().getName()
										+ ", "
										+ ((PajeUserLink) ent)
												.getEndContainer().getName());
						break;
					default:
						break;

					}
				}

			} 

		}

	}

	public void simulate(PajeObject data) throws Exception {
		PajeTraceEvent event = (PajeTraceEvent) data;
		setLastKnownTime(event);
		PajeEventId eventId = event.getPajeEventDef().getPajeEventId();
		callEventMethod(eventId, event);

	}

	public void callEventMethod(PajeEventId eventId, PajeTraceEvent event)
			throws Exception {
		switch (eventId) {
		case PajeDefineContainerType:
			pajeDefineContainerType(event);
			break;
		case PajeDefineStateType:
			pajeDefineStateType(event);
			break;
		case PajeDefineEventType:
			pajeDefineEventType(event);
			break;
		case PajeDefineVariableType:
			pajeDefineVariableType(event);
			break;
		case PajeDefineLinkType:
			pajeDefineLinkType(event);
			break;
		case PajeDefineEntityValue:
			pajeDefineEntityValue(event);
			break;
		case PajeCreateContainer:
			pajeCreateContainer(event);
			break;
		case PajeDestroyContainer:
			pajeDestroyContainer(event);
			break;
		case PajeSetState:
			pajeSetState(event);
			break;
		case PajePushState:
			pajePushState(event);
			break;
		case PajePopState:
			pajePopState(event);
			break;
		case PajeResetState:
			pajeResetState(event);
			break;
		case PajeNewEvent:
			pajeNewEvent(event);
			break;
		case PajeSetVariable:
			pajeSetVariable(event);
			break;
		case PajeAddVariable:
			pajeAddVariable(event);
			break;
		case PajeSubVariable:
			pajeSubVariable(event);
			break;
		case PajeStartLink:
			pajeStartLink(event);
			break;
		case PajeEndLink:
			pajeEndLink(event);
			break;
		default:
			break;
		}
	}

	private void pajeDefineContainerType(PajeTraceEvent event) throws Exception {
		String name = event.valueForField(PajeFieldName.Name);
		String type = event.valueForField(PajeFieldName.Type);
		String alias = event.valueForField(PajeFieldName.Alias);
		int line = event.getLine();

		// check if name is allowed
		if (name.equals("0")) {
			throw new Exception("Name can't be 0");
		}
		if (typeNamesMap.containsKey(name)) {
			throw new Exception("Name " + name + " defined in line " + line
					+ " already exists");
		}

		PajeContainerType containerType;
		if (typeMap.containsKey(type)) {
			containerType = (PajeContainerType) typeMap.get(type);
		} else {
			throw new Exception("Container type " + type + " defined in line "
					+ line + " does not exist");
		}

		String identifier = alias.isEmpty() ? name : alias;
		PajeContainerType newType;
		if (typeMap.containsKey(identifier)) {
			throw new Exception("Container type " + identifier
					+ " defined in line " + line + " already exists");
		} else {
			newType = new PajeContainerType(name, alias, containerType);
		}
		typeMap.put(newType.getId(), newType);
		typeNamesMap.put(newType.getName(), newType);

		// add children to the parent container type
		containerType.addChildrenType(name, alias, newType);
	}

	private void pajeDefineStateType(PajeTraceEvent event) throws Exception {
		String name = event.valueForField(PajeFieldName.Name);
		String type = event.valueForField(PajeFieldName.Type);
		String alias = event.valueForField(PajeFieldName.Alias);
		int line = event.getLine();

		if (typeNamesMap.containsKey(name)) {
			throw new Exception("Name " + name + " defined in line " + line
					+ " already exists");
		}

		PajeContainerType containerType;
		if (typeMap.containsKey(type)) {
			containerType = (PajeContainerType) typeMap.get(type);
		} else {
			throw new Exception("Container type " + type + " defined in line "
					+ line + " does not exist");
		}

		String identifier = alias.isEmpty() ? name : alias;
		PajeStateType newType;
		if (typeMap.containsKey(identifier)) {
			throw new Exception("State type " + identifier
					+ " defined in line " + line + " already exists");
		} else {
			newType = new PajeStateType(name, alias, containerType);
		}
		typeMap.put(newType.getId(), newType);
		typeNamesMap.put(newType.getName(), newType);

		// add children to the parent container type
		containerType.addChildrenType(name, alias, newType);
	}

	private void pajeDefineEventType(PajeTraceEvent event) throws Exception {
		String name = event.valueForField(PajeFieldName.Name);
		String type = event.valueForField(PajeFieldName.Type);
		String alias = event.valueForField(PajeFieldName.Alias);
		int line = event.getLine();

		if (typeNamesMap.containsKey(name)) {
			throw new Exception("Name already exists");
		}

		// parent container
		PajeContainerType containerType;
		if (typeMap.containsKey(type)) {
			containerType = (PajeContainerType) typeMap.get(type);
		} else {
			throw new Exception("Container type " + type + " defined in line "
					+ line + "does not exist");
		}

		String identifier = alias.isEmpty() ? name : alias;
		PajeEventType newType;
		if (typeMap.containsKey(identifier)) {
			throw new Exception("Event type " + identifier
					+ " defined in line " + line + "already exists");
		} else {
			newType = new PajeEventType(name, alias, containerType);
		}
		typeMap.put(newType.getId(), newType);
		typeNamesMap.put(newType.getName(), newType);

		// add children to the parent container type
		containerType.addChildrenType(name, alias, newType);
	}

	private void pajeDefineVariableType(PajeTraceEvent event) throws Exception {
		String name = event.valueForField(PajeFieldName.Name);
		String type = event.valueForField(PajeFieldName.Type);
		String alias = event.valueForField(PajeFieldName.Alias);
		// optional
		String color = event.valueForField(PajeFieldName.Color);
		int line = event.getLine();

		if (typeNamesMap.containsKey(name)) {
			throw new Exception("Name already exists");
		}

		PajeContainerType containerType;
		if (typeMap.containsKey(type)) {
			containerType = (PajeContainerType) typeMap.get(type);
		} else {
			throw new Exception("Container type " + type + " defined in line "
					+ line + "does not exist");
		}

		PajeColor pajeColor;
		if (!color.isEmpty()) {
			pajeColor = getColor(color, event);
		}

		pajeColor = null;

		String identifier = alias.isEmpty() ? name : alias;
		PajeVariableType newType;
		if (typeMap.containsKey(identifier)) {
			throw new Exception("Variable type " + identifier
					+ " defined in line " + line + "already exists");
		} else {
			newType = new PajeVariableType(name, alias, containerType,
					pajeColor);
		}

		typeMap.put(newType.getId(), newType);
		typeNamesMap.put(newType.getName(), newType);

		// add children to the parent container type
		containerType.addChildrenType(name, alias, newType);
	}

	private void pajeDefineLinkType(PajeTraceEvent event) throws Exception {
		String name = event.valueForField(PajeFieldName.Name);
		String type = event.valueForField(PajeFieldName.Type);
		String alias = event.valueForField(PajeFieldName.Alias);
		String startType = event
				.valueForField(PajeFieldName.StartContainerType);
		String endType = event.valueForField(PajeFieldName.EndContainerType);
		int line = event.getLine();

		if (typeNamesMap.containsKey(name)) {
			throw new Exception("Name " + name + " defined in line " + line
					+ " already exists");
		}

		PajeContainerType containerType;
		if (typeMap.containsKey(type)) {
			containerType = (PajeContainerType) typeMap.get(type);
		} else {
			throw new Exception("Container type " + type + " defined in line "
					+ line + " does not exist");
		}

		// check if start and end containers exist
		if (!typeMap.containsKey(startType)) {
			throw new Exception("Container type " + startType
					+ " defined in line " + line + " does not exist");
		}

		if (!typeMap.containsKey(endType)) {
			throw new Exception("Container type " + endType
					+ " defined in line " + line + " does not exist");
		}

		PajeContainerType start = (PajeContainerType) typeMap.get(startType);
		PajeContainerType end = (PajeContainerType) typeMap.get(endType);

		// check if they are container types
		if (!start.getNature().equals(PajeTypeNature.ContainerType))
			throw new Exception("Type " + startType
					+ " defined as start container in line " + line
					+ " is not a container type");
		if (!end.getNature().equals(PajeTypeNature.ContainerType))
			throw new Exception("Type " + endType
					+ " defined as end container in line " + line
					+ " is not a container type");

		// check if Type is a common ancestral of start and end
		if (!start.hasAncestral(containerType))
			throw new Exception("Container type " + startType + " in line "
					+ line + " does not have " + type + " as ancestral");
		if (!end.hasAncestral(containerType))
			throw new Exception("Container type " + endType + " in line "
					+ line + " does not have " + type + " as ancestral");

		String identifier = alias.isEmpty() ? name : alias;
		PajeLinkType newType;
		if (typeMap.containsKey(identifier)) {
			throw new Exception("Link type " + identifier + " defined in line "
					+ line + " already exists");
		} else {
			newType = new PajeLinkType(name, alias, containerType, start, end);
		}
		typeMap.put(newType.getId(), newType);
		typeNamesMap.put(newType.getName(), newType);

		// add children to the parent container type
		containerType.addChildrenType(name, alias, newType);
	}

	private void pajeDefineEntityValue(PajeTraceEvent event) throws Exception {
		String name = event.valueForField(PajeFieldName.Name);
		String type = event.valueForField(PajeFieldName.Type);
		String alias = event.valueForField(PajeFieldName.Alias);
		// optional
		String color = event.valueForField(PajeFieldName.Color);
		int line = event.getLine();

		if (typeNamesMap.containsKey(name)) {
			throw new Exception("Name " + name + " defined in line " + line
					+ " already exists");
		}

		PajeCategorizedType targetType;
		if (typeMap.containsKey(type)) {
			targetType = (PajeCategorizedType) typeMap.get(type);
		} else {
			throw new Exception("Type " + type + " defined in line " + line
					+ " does not exist");
		}

		// validates color if exists
		PajeColor pajeColor = null;
		if (!color.isEmpty()) {
			pajeColor = getColor(color, event);
		}

		//pajeColor = null;

		// check if type is an acceptable type
		if (targetType.getNature().equals(PajeTypeNature.ContainerType))
			throw new Exception(
					"Type "
							+ type
							+ " defined in line "
							+ line
							+ " (which is a container type) is not a valid type to define the value");
		if (targetType.getNature().equals(PajeTypeNature.VariableType))
			throw new Exception(
					"Type "
							+ type
							+ " defined in line "
							+ line
							+ " (which is a variable type) is not a valid type to define the value");

		String identifier = alias.isEmpty() ? name : alias;
		if (targetType.getValues().containsKey(identifier)) {
			throw new Exception("Trying to redefine the value " + identifier
					+ " for " + type + " in line " + line);
		} else {
			targetType.addValue(name, alias, pajeColor);
		}

	}

	private void pajeCreateContainer(PajeTraceEvent event) throws Exception {
		String name = event.valueForField(PajeFieldName.Name);
		String type = event.valueForField(PajeFieldName.Type);
		String alias = event.valueForField(PajeFieldName.Alias);
		String container = event.valueForField(PajeFieldName.Container);
		int line = event.getLine();

		// name cannot be 0
		if (name.equals("0")) {
			throw new Exception("The container name 0 can't be used (line "
					+ line + ")");
		}

		// check if container type exists and is a container
		if (!typeMap.containsKey(type)) {
			throw new Exception("The container type " + type
					+ " defined in line " + line + " is not defined");
		}

		if (!typeMap.get(type).getNature().equals(PajeTypeNature.ContainerType)) {
			throw new Exception("Type " + type + " defined in line " + line
					+ " used to create a container is not a container");
		}

		PajeType pajeType = typeMap.get(type);

		// get the parent container
		PajeContainer parentContainer;
		if (!contMap.containsKey(container))
			throw new Exception("Container " + container + " defined in line "
					+ line + " does not exist");
		else
			parentContainer = contMap.get(container);

		PajeContainerType parentType = (PajeContainerType) typeMap
				.get(parentContainer.getType().getAlias());

		// check if type is child of container type
		// direct child or has ancestral?
		if (!pajeType.hasAncestral(parentType))
			throw new Exception("Container type " + type + " defined in line "
					+ line + " is not a child type for the container type of"
					+ container);

		String identifier = alias.isEmpty() ? name : alias;
		if (contMap.containsKey(identifier))
			throw new Exception("Container " + identifier + " defined in line "
					+ line + " already exists");

		// time is in lastKnownTime (defined in simulate method)
		PajeContainer newContainer = new PajeContainer(lastKnownTime, name,
				alias, parentContainer, pajeType, event);
		contMap.put(identifier, newContainer);
		contNamesMap.put(name, newContainer);

		parentContainer.addChildren(identifier, newContainer);

	}

	public void pajeDestroyContainer(PajeTraceEvent event) throws Exception {
		String name = event.valueForField(PajeFieldName.Name);
		String type = event.valueForField(PajeFieldName.Type);
		int line = event.getLine();

		// check if container type exists and is a container
		if (!typeMap.containsKey(type)) {
			throw new Exception("The container type " + type
					+ " defined in line " + line + " is not defined");
		}
		if (!typeMap.get(type).getNature().equals(PajeTypeNature.ContainerType)) {
			throw new Exception("Type " + type
					+ " used to find container in line " + line
					+ " is not a container");
		}
		PajeType pajeType = typeMap.get(type);

		// search for container to be destroyed
		PajeContainer container;
		if (contMap.containsKey(name)) {
			container = contMap.get(name);
		} else
			throw new Exception("Container to be destroyed " + name
					+ " defined in line " + line + " does not exist");

		// check if container type is correct
		if (!container.getType().equals(pajeType)) {
			throw new Exception("Wrong container type " + pajeType.getAlias()
					+ " for container " + container.getName() + " in line "
					+ line);
		}

		// create event to sent to demuxer (necessary?? could we use the trace
		// event?)
		PajeDestroyContainerEvent destroyEvent = new PajeDestroyContainerEvent(
				event, container, pajeType, lastKnownTime);
		container.demuxer(destroyEvent);
	}

	public void pajeSetState(PajeTraceEvent event) throws Exception {
		String containerName = event.valueForField(PajeFieldName.Container);
		String type = event.valueForField(PajeFieldName.Type);
		String value = event.valueForField(PajeFieldName.Value);
		int line = event.getLine();

		// check if container type exists and is a state type
		if (!typeMap.containsKey(type)) {
			throw new Exception("The type " + type + " refered in line " + line
					+ " is not defined");
		}
		if (!typeMap.get(type).getNature().equals(PajeTypeNature.StateType)) {
			throw new Exception("Type " + type + " used in line " + line
					+ " is not a state type");
		}
		PajeStateType pajeType = (PajeStateType) typeMap.get(type);

		// check if container exists
		PajeContainer container;
		if (contMap.containsKey(containerName)) {
			container = contMap.get(containerName);
		} else
			throw new Exception("Container " + containerName
					+ " defined in line " + line + " does not exist");

		// check if type is a child of container type
		PajeContainerType containerType = (PajeContainerType) container
				.getType();
		if (!pajeType.hasAncestral(containerType)) {
			throw new Exception("Type " + type
					+ " is not a child type of the container type of "
					+ containerName);
		}

		// check if the value was previously declared
		PajeValue pajeValue = null;
		if (!pajeType.hasValueForIdentifier(value)) {
			pajeType.addValue(value, value, null);
		}

		pajeValue = pajeType.valueForIdentifier(value);

		PajeSetStateEvent setStateEvent = new PajeSetStateEvent(event,
				container, pajeType, lastKnownTime, pajeValue);
		container.demuxer(setStateEvent);
	}

	public void pajePushState(PajeTraceEvent event) throws Exception {
		String containerName = event.valueForField(PajeFieldName.Container);
		String type = event.valueForField(PajeFieldName.Type);
		String value = event.valueForField(PajeFieldName.Value);
		int line = event.getLine();

		// check if container type exists and is a state type
		if (!typeMap.containsKey(type)) {
			throw new Exception("The type " + type + " refered in line " + line
					+ " is not defined");
		}
		if (!typeMap.get(type).getNature().equals(PajeTypeNature.StateType)) {
			throw new Exception("Type " + type + " used in line " + line
					+ " is not a state type");
		}
		PajeStateType pajeType = (PajeStateType) typeMap.get(type);

		// check if container exists
		PajeContainer container;
		if (contMap.containsKey(containerName)) {
			container = contMap.get(containerName);
		} else
			throw new Exception("Container " + containerName
					+ " defined in line " + line + " does not exist");

		// check if type is a child of container type
		PajeContainerType containerType = (PajeContainerType) container
				.getType();
		if (!pajeType.hasAncestral(containerType)) {
			throw new Exception("Type " + type
					+ " is not a child type of the container type of "
					+ containerName);
		}

		// check if the value was previously declared
		// commented: exception following the Note in the paje documentation
		PajeValue pajeValue = null;
		if (!pajeType.hasValueForIdentifier(value)) {
			pajeType.addValue(value, value, null);
			pajeValue = pajeType.valueForIdentifier(value);
			// throw new Exception
			// ("There is no existing value to be saved in line " + line);
		} else {
			pajeValue = pajeType.valueForIdentifier(value);
		}

		PajePushStateEvent setStateEvent = new PajePushStateEvent(event,
				container, pajeType, lastKnownTime, pajeValue);
		container.demuxer(setStateEvent);
	}

	public void pajePopState(PajeTraceEvent event) throws Exception {
		String containerName = event.valueForField(PajeFieldName.Container);
		String type = event.valueForField(PajeFieldName.Type);
		int line = event.getLine();

		// check if type exists and is a state type
		if (!typeMap.containsKey(type)) {
			throw new Exception("The type " + type + " refered in line " + line
					+ " is not defined");
		}
		if (!typeMap.get(type).getNature().equals(PajeTypeNature.StateType)) {
			throw new Exception("Type " + type + " used in line " + line
					+ " is not a state type");
		}
		PajeStateType pajeType = (PajeStateType) typeMap.get(type);

		// check if container exists
		PajeContainer container;
		if (contMap.containsKey(containerName)) {
			container = contMap.get(containerName);
		} else
			throw new Exception("Container " + containerName
					+ " defined in line " + line + " does not exist");

		PajePopStateEvent setStateEvent = new PajePopStateEvent(event,
				container, pajeType, lastKnownTime);
		container.demuxer(setStateEvent);
	}

	public void pajeResetState(PajeTraceEvent event) throws Exception {
		String containerName = event.valueForField(PajeFieldName.Container);
		String type = event.valueForField(PajeFieldName.Type);
		int line = event.getLine();

		if (!typeMap.get(type).getNature().equals(PajeTypeNature.StateType)) {
			throw new Exception("Type " + type + " used in line " + line
					+ " is not a state type");
		}
		PajeStateType pajeType = (PajeStateType) typeMap.get(type);

		// check if container exists
		PajeContainer container;
		if (contMap.containsKey(containerName)) {
			container = contMap.get(containerName);
		} else
			throw new Exception("Container " + containerName
					+ " defined in line " + line + " does not exist");

		// check if type is a child of container type
		PajeContainerType containerType = (PajeContainerType) container
				.getType();
		if (!pajeType.hasAncestral(containerType)) {
			throw new Exception("Type " + type
					+ " is not a child type of the container type of "
					+ containerName);
		}

		PajeResetStateEvent resetStateEvent = new PajeResetStateEvent(event,
				container, pajeType, lastKnownTime);
		container.demuxer(resetStateEvent);

	}

	public void pajeNewEvent(PajeTraceEvent event) throws Exception {
		String type = event.valueForField(PajeFieldName.Type);
		String containerName = event.valueForField(PajeFieldName.Container);
		String value = event.valueForField(PajeFieldName.Value);
		int line = event.getLine();

		if (!typeMap.get(type).getNature().equals(PajeTypeNature.EventType)) {
			throw new Exception("Type " + type + " used in line " + line
					+ " is not an event type");
		}
		PajeEventType pajeType = (PajeEventType) typeMap.get(type);

		// check if container exists
		PajeContainer container;
		if (contMap.containsKey(containerName)) {
			container = contMap.get(containerName);
		} else
			throw new Exception("Container " + containerName
					+ " defined in line " + line + " does not exist");

		PajeContainerType containerType = (PajeContainerType) container
				.getType();
		if (!pajeType.hasAncestral(containerType)) {
			throw new Exception("Type " + type
					+ " is not a child type of the container type of "
					+ containerName);
		}

		// check if the value was previously declared
		// commented: exception following the Note in the paje documentation
		PajeValue pajeValue = null;
		if (!pajeType.hasValueForIdentifier(value)) {
			pajeType.addValue(value, value, null);
			// throw new Exception
			// ("There is no existing value to be saved in line " + line);
		}
		pajeValue = pajeType.valueForIdentifier(value);

		PajeNewEventEvent newEventEvent = new PajeNewEventEvent(event,
				container, pajeType, lastKnownTime, pajeValue);
		container.demuxer(newEventEvent);
	}

	public void pajeSetVariable(PajeTraceEvent event) throws Exception {
		String type = event.valueForField(PajeFieldName.Type);
		String containerName = event.valueForField(PajeFieldName.Container);
		String value = event.valueForField(PajeFieldName.Value);
		int line = event.getLine();

		if (!typeMap.get(type).getNature().equals(PajeTypeNature.VariableType)) {
			throw new Exception("Type " + type + " used in line " + line
					+ " is not a variable type");
		}
		PajeVariableType pajeType = (PajeVariableType) typeMap.get(type);

		PajeContainer container;
		if (contMap.containsKey(containerName)) {
			container = contMap.get(containerName);
		} else
			throw new Exception("Container " + containerName
					+ " defined in line " + line + " does not exist");

		PajeContainerType containerType = (PajeContainerType) container
				.getType();
		if (!pajeType.hasAncestral(containerType)) {
			throw new Exception("Type " + type
					+ " is not a child type of the container type of "
					+ containerName);
		}

		double val = Double.parseDouble(value);

		PajeSetVariableEvent setVariableEvent = new PajeSetVariableEvent(event,
				container, pajeType, lastKnownTime, val);
		container.demuxer(setVariableEvent);

	}

	public void pajeAddVariable(PajeTraceEvent event) throws Exception {
		String type = event.valueForField(PajeFieldName.Type);
		String containerName = event.valueForField(PajeFieldName.Container);
		String value = event.valueForField(PajeFieldName.Value);
		int line = event.getLine();

		if (!typeMap.get(type).getNature().equals(PajeTypeNature.VariableType)) {
			throw new Exception("Type " + type + " used in line " + line
					+ " is not a variable type");
		}
		PajeVariableType pajeType = (PajeVariableType) typeMap.get(type);

		PajeContainer container;
		if (contMap.containsKey(containerName)) {
			container = contMap.get(containerName);
		} else
			throw new Exception("Container " + containerName
					+ " defined in line " + line + " does not exist");

		PajeContainerType containerType = (PajeContainerType) container
				.getType();
		if (!pajeType.hasAncestral(containerType)) {
			throw new Exception("Type " + type
					+ " is not a child type of the container type of "
					+ containerName);
		}

		double val = Double.parseDouble(value);

		PajeAddVariableEvent addVariableEvent = new PajeAddVariableEvent(event,
				container, pajeType, lastKnownTime, val);
		container.demuxer(addVariableEvent);
	}

	public void pajeSubVariable(PajeTraceEvent event) throws Exception {
		String type = event.valueForField(PajeFieldName.Type);
		String containerName = event.valueForField(PajeFieldName.Container);
		String value = event.valueForField(PajeFieldName.Value);
		int line = event.getLine();

		if (!typeMap.get(type).getNature().equals(PajeTypeNature.VariableType)) {
			throw new Exception("Type " + type + " used in line " + line
					+ " is not a variable type");
		}
		PajeVariableType pajeType = (PajeVariableType) typeMap.get(type);

		PajeContainer container;
		if (contMap.containsKey(containerName)) {
			container = contMap.get(containerName);
		} else
			throw new Exception("Container " + containerName
					+ " defined in line " + line + " does not exist");

		PajeContainerType containerType = (PajeContainerType) container
				.getType();
		if (!pajeType.hasAncestral(containerType)) {
			throw new Exception("Type " + type
					+ " is not a child type of the container type of "
					+ containerName);
		}

		double val = Double.parseDouble(value);

		PajeSubVariableEvent subVariableEvent = new PajeSubVariableEvent(event,
				container, pajeType, lastKnownTime, val);
		container.demuxer(subVariableEvent);
	}

	public void pajeStartLink(PajeTraceEvent event) throws Exception {
		String type = event.valueForField(PajeFieldName.Type);
		String containerName = event.valueForField(PajeFieldName.Container);
		String startContainerName = event
				.valueForField(PajeFieldName.StartContainer);
		String value = event.valueForField(PajeFieldName.Value);
		String key = event.valueForField(PajeFieldName.Key);
		int line = event.getLine();

		PajeContainer container;
		if (contMap.containsKey(containerName)) {
			container = contMap.get(containerName);
		} else
			throw new Exception("Container " + containerName
					+ " defined in line " + line + " does not exist");

		PajeContainer startContainer = null;
		if (contMap.containsKey(startContainerName)) {
			startContainer = contMap.get(startContainerName);
		} else
			throw new Exception("Container " + startContainerName
					+ " defined in line " + line + " does not exist");

		if (!typeMap.get(type).getNature().equals(PajeTypeNature.LinkType)) {
			throw new Exception("Type " + type + " used in line " + line
					+ " is not a link type");
		}
		PajeLinkType pajeType = (PajeLinkType) typeMap.get(type);

		PajeContainerType containerType = (PajeContainerType) container
				.getType();
		if (!pajeType.hasAncestral(containerType)) {
			throw new Exception("Type " + type
					+ " is not a child type of the container type of "
					+ containerName);
		}

		// check if start type defined in the link type is the same as the start
		// container type
		if (!pajeType.getStartType().equals(startContainer.getType()))
			throw new Exception("Type " + type + " does not have "
					+ startContainerName + " as start container type in line "
					+ line);

		// check if the value was previously declared
		// commented: exception following the Note in the paje documentation
		PajeValue pajeValue = null;
		if (!pajeType.hasValueForIdentifier(value)) {
			pajeType.addValue(value, value, null);
			// throw new Exception
			// ("There is no existing value to be saved in line " + line);
		}
		pajeValue = pajeType.valueForIdentifier(value);

		PajeStartLinkEvent startLinkEvent = new PajeStartLinkEvent(event,
				container, startContainer, pajeType, lastKnownTime, pajeValue,
				key);
		container.demuxer(startLinkEvent);

	}

	public void pajeEndLink(PajeTraceEvent event) throws Exception {
		String type = event.valueForField(PajeFieldName.Type);
		String containerName = event.valueForField(PajeFieldName.Container);
		String endContainerName = event
				.valueForField(PajeFieldName.EndContainer);
		String value = event.valueForField(PajeFieldName.Value);
		String key = event.valueForField(PajeFieldName.Key);
		int line = event.getLine();

		PajeContainer container;
		if (contMap.containsKey(containerName)) {
			container = contMap.get(containerName);
		} else
			throw new Exception("Container " + containerName
					+ " defined in line " + line + " does not exist");

		PajeContainer endContainer = null;
		if (contMap.containsKey(endContainerName)) {
			endContainer = contMap.get(endContainerName);
		} else
			throw new Exception("Container " + endContainerName
					+ " defined in line " + line + " does not exist");

		if (!typeMap.get(type).getNature().equals(PajeTypeNature.LinkType)) {
			throw new Exception("Type " + type + " used in line " + line
					+ " is not a link type");
		}
		PajeLinkType pajeType = (PajeLinkType) typeMap.get(type);

		PajeContainerType containerType = (PajeContainerType) container
				.getType();
		if (!pajeType.hasAncestral(containerType)) {
			throw new Exception("Type " + type
					+ " is not a child type of the container type of "
					+ containerName);
		}

		// check if start type defined in the link type is the same as the start
		// container type
		if (!pajeType.getEndType().equals(endContainer.getType()))
			throw new Exception("Type " + type + " does not have "
					+ endContainerName + " as end container type in line "
					+ line);

		// check if the value was previously declared
		// commented: exception following the Note in the paje documentation
		PajeValue pajeValue = null;
		if (!pajeType.hasValueForIdentifier(value)) {
			pajeType.addValue(value, value, null);
			// throw new Exception
			// ("There is no existing value to be saved in line " + line);
		}
		pajeValue = pajeType.valueForIdentifier(value);

		PajeEndLinkEvent endLinkEvent = new PajeEndLinkEvent(event, container,
				endContainer, pajeType, lastKnownTime, pajeValue, key);
		container.demuxer(endLinkEvent);

	}

}
