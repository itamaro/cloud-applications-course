
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class FileReaderSpout implements IRichSpout {
  private SpoutOutputCollector _collector;
  private TopologyContext context;
  private FileReader filereader;
  private BufferedReader bufferedreader;

  @Override
  public void open(Map conf, TopologyContext context,
                   SpoutOutputCollector collector) {
    try {
      this.filereader = new FileReader((String)conf.get("data"));
      bufferedreader = new BufferedReader(this.filereader);
      this.context = context;
      this._collector = collector;
    } catch (FileNotFoundException e) {
      // ?
    } catch (IOException e) {
      // ?
    }
  }

  @Override
  public void nextTuple() {
    try {
      String line = bufferedreader.readLine();
      if (line != null) {
        _collector.emit(new Values(line));
      } else {
        Utils.sleep(5000);
      }
    } catch (IOException e) {
      // ?
    }
    Utils.sleep(10);
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {

    declarer.declare(new Fields("word"));

  }

  @Override
  public void close() {
    try {
      this.filereader.close();
    } catch (IOException e) {
      // ?
    }
  }


  @Override
  public void activate() {
  }

  @Override
  public void deactivate() {
  }

  @Override
  public void ack(Object msgId) {
  }

  @Override
  public void fail(Object msgId) {
  }

  @Override
  public Map<String, Object> getComponentConfiguration() {
    return null;
  }
}
