package components;

public class ComponentSender {
    // @todo avoir la liste des observers en fonction des events et les envoyer
/*
public interface ComponentObserver {
    public static enum ComponentEvent {
        LOAD_CONVERSATION,
        SHOW_CONVERSATION,
        HIDE_CONVERSATION,
        QUEST_LOCATION_DISCOVERED,
        ENEMY_SPAWN_LOCATION_CHANGED,
        PLAYER_HAS_MOVED
    }

    void onNotify(final String value, ComponentEvent event);
}
**************************************************************


    private Array<ComponentObserver> _observers;

    public ComponentSubject(){
        _observers = new Array<ComponentObserver>();
    }

    public void addObserver(ComponentObserver conversationObserver){
        _observers.add(conversationObserver);
    }

    public void removeObserver(ComponentObserver conversationObserver){
        _observers.removeValue(conversationObserver, true);
    }

    public void removeAllObservers(){
        for(ComponentObserver observer: _observers){
            _observers.removeValue(observer, true);
        }
    }

 */


    public void sendMessage(String event, String message) {

    }
}
