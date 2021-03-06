package james.a.grant.wagon;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import james.a.grant.wagon.testobjects.CrateHolder;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(TestRunner.class)
public class WagonPreferenceTests {

    @WoodBox(key = "key", preference = false)
    public String testBox = "testBox";

    @Mock
    private SharedPreferences mockPreferences;
    @Mock
    private Editor mockEditor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockPreferences.edit()).thenReturn(mockEditor);
    }

    @Test
    public void itPacksAllTheBoxes() {
        james.a.grant.wagon.testobjects.Boxes boxes = new james.a.grant.wagon.testobjects.Boxes("s", 1, 2, 3, 4);
        Wagon<james.a.grant.wagon.testobjects.Boxes> wagon = new Wagon<james.a.grant.wagon.testobjects.Boxes>(boxes.getClass(), boxes);
        wagon.pack(mockPreferences);
        verify(mockEditor).putString("s", "s");
        verify(mockEditor).putInt("i", 1);
        verify(mockEditor).putFloat("f", 2);
        verify(mockEditor).putString("d", PreferenceCollector.KEY_DOUBLE + PreferenceCollector.DELIM + 3.0d);
        verify(mockEditor).putLong("l", 4);

        wagon.unpack(mockPreferences);
        verify(mockPreferences).getString("s", null);
        verify(mockPreferences).getInt("i", 0);
        verify(mockPreferences).getFloat("f", 0);
        verify(mockPreferences).getString("d", null);
        verify(mockPreferences).getLong("l", 0);
    }

    @Test
    public void itPacksTheCrate() {
        String crateKey = "crateKey";
        CrateHolder crateHolder = new CrateHolder("s", 1, 2, 3, 4);
        Wagon<CrateHolder> wagon = new Wagon<CrateHolder>(crateHolder.getClass(), crateHolder);
        wagon.pack(mockPreferences);
        verify(mockEditor).putString(crateKey + "s", "s");
        verify(mockEditor).putInt(crateKey + "i", 1);
        verify(mockEditor).putFloat(crateKey + "f", 2);
        verify(mockEditor).putString(crateKey + "d", PreferenceCollector.KEY_DOUBLE + PreferenceCollector.DELIM + 3.0d);
        verify(mockEditor).putLong(crateKey + "l", 4);
        //
        crateHolder = new CrateHolder();
        assertThat(crateHolder.testCrate.s, nullValue());
        Wagon<CrateHolder> wagon2 = new Wagon<CrateHolder>(crateHolder.getClass(), crateHolder);
        wagon2.unpack(mockPreferences);
        verify(mockPreferences).getString(crateKey + "s", null);
        verify(mockPreferences).getInt(crateKey + "i", 0);
        verify(mockPreferences).getFloat(crateKey + "f", 0);
        verify(mockPreferences).getString(crateKey + "d", null);
        verify(mockPreferences).getLong(crateKey + "l", 0);
    }

    @Test
    public void itDoesNotPackNonPreferences() {
        Wagon<WagonPreferenceTests> wagon = new Wagon<WagonPreferenceTests>(this.getClass(), this);
        wagon.pack(mockPreferences);
        verify(mockEditor, never()).putString("key", "testBox");
    }
}
