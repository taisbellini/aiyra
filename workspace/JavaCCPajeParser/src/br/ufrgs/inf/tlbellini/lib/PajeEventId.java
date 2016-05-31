package br.ufrgs.inf.tlbellini.lib;

public enum PajeEventId {
    PajeDefineContainerType,
    PajeDefineEventType,
    PajeDefineStateType,
    PajeDefineVariableType,
    PajeDefineLinkType,
    PajeDefineEntityValue,
    PajeCreateContainer,
    PajeDestroyContainer,
    PajeNewEvent,
    PajeSetState,
    PajePushState,
    PajePopState,
    PajeResetState,
    PajeSetVariable,
    PajeAddVariable,
    PajeSubVariable,
    PajeStartLink,
    PajeEndLink,
    PajeTraceFile,
    PajeEventIdCount,
    PajeUnknown
}
