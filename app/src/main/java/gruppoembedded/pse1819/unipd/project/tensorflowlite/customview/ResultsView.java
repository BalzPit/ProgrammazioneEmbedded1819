package gruppoembedded.pse1819.unipd.project.tensorflowlite.customview;

import java.util.List;
import gruppoembedded.pse1819.unipd.project.tensorflowlite.tflite.Classifier.Recognition;

public interface ResultsView {
  public void setResults(final List<Recognition> results);
}
