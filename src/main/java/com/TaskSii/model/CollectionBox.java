package com.TaskSii.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Map;


// TODO check if amount of money can be transfered multiple times
@Entity
@Table(name = "collection_box")
public class CollectionBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean empty = true;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private FundraisingEvent fundraisingEvent;

    @ElementCollection
    @CollectionTable(name = "box_money", joinColumns = @JoinColumn(name = "box_id"))
    @MapKeyColumn(name = "currency")
    @Column(name = "amount")
    private Map<Currency, BigDecimal> money;

    public CollectionBox(boolean empty, Map<Currency, BigDecimal> money) {
        this.empty = empty;
        this.money = money;
    }

    public CollectionBox() {
    }

    public Map<Currency, BigDecimal> getMoney() {
        return money;
    }

    public void setMoney(Map<Currency, BigDecimal> money) {
        this.money = money;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public FundraisingEvent getFundraisingEvent() {
        return fundraisingEvent;
    }

    public void setFundraisingEvent(FundraisingEvent fundraisingEvent) {
        this.fundraisingEvent = fundraisingEvent;
    }
}
