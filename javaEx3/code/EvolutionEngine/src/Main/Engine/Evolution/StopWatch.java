package Main.Engine.Evolution;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class StopWatch implements Serializable {

    private long currentRead;
    private long end;
    private long cycleStart;
    private long cycleEnd;
    private boolean running;

    public StopWatch()
    {
        this.currentRead = 0;
        this.end = 0;
        this.running = false;
    }

    public StopWatch(long endTime, TimeUnit endTimeUnit)
    {
        this.currentRead = 0;
        this.end = TimeUnit.MILLISECONDS.convert(endTime, endTimeUnit);
        this.running = false;
    }

    public boolean start()
    {
        if(running)
            return false;
        else
        {
            cycleStart = System.currentTimeMillis();
            running = true;
            return true;
        }
    }

    public void setStopTime(long endTime, TimeUnit endTimeUnit)
    {
        this.end = TimeUnit.MILLISECONDS.convert(endTime, endTimeUnit);
    }

    public long getStopTime(TimeUnit endTimeUnit)
    {
        return endTimeUnit.convert(end, TimeUnit.MILLISECONDS);
    }

    public boolean stop()
    {
        if(!running)
            return false;
        else
        {
            cycleEnd = System.currentTimeMillis();
            currentRead += (cycleEnd - cycleStart);
            cycleStart = 0;
            cycleEnd = 0;
            running = false;
            return true;
        }
    }

    public boolean didTimePass()
    {
        if(running)
        {
            cycleEnd = System.currentTimeMillis();
            return currentRead + (cycleEnd - cycleStart) >= end;
        }
        else
            return currentRead >= end;
    }

    public long getCurrentRead(TimeUnit timeUnitToRead)
    {
        long readInMillis = 0;

        if(running)
        {
            cycleEnd = System.currentTimeMillis();
            readInMillis += currentRead + (cycleEnd - cycleStart);
        }
        else
            readInMillis += currentRead;

        return timeUnitToRead.convert(readInMillis, TimeUnit.MILLISECONDS);
    }

    public void stopAndReset()
    {
        running = false;
        currentRead = 0;
    }
}
