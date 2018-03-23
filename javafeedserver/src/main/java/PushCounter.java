/**
 * Created by Administrator on 15/2/2017.
 */
public class PushCounter {

    String counterid;
    String symbol;
    String bid;
    String ask;
    String last;
    String change;
    String high;
    String low;
    String prev_close;
    String open;
    String time;
    Boolean hasupdate;

    public PushCounter(Counter counter) {
        this.counterid = counter.counterid;
        this.symbol = counter.symbol;
        this.bid = counter.bid;
        this.ask = counter.ask;
        this.last = counter.last;
        this.change = counter.change;
        this.high = counter.high;
        this.low = counter.low;
        this.prev_close = counter.prev_close;
        this.open = counter.open;
        this.time = counter.time;
        this.hasupdate = counter.hasupdate;
    }

    public String getCounterid() {
        return counterid;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getBid() {
        return bid;
    }

    public String getAsk() {
        return ask;
    }

    public String getLast() {
        return last;
    }

    public String getChange() {
        return change;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getPrev_close() {
        return prev_close;
    }

    public String getOpen() {
        return open;
    }

    public String getTime() {
        return time;
    }

    public Boolean getHasupdate() {
        return hasupdate;
    }

    @Override
    public String toString() {
        return "PushCounter{" +
                "counterid='" + counterid + '\'' +
                ", symbol='" + symbol + '\'' +
                ", bid='" + bid + '\'' +
                ", ask='" + ask + '\'' +
                ", last='" + last + '\'' +
                ", change='" + change + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", prev_close='" + prev_close + '\'' +
                ", open='" + open + '\'' +
                ", time='" + time + '\'' +
                ", hasupdate=" + hasupdate +
                '}';
    }
}
