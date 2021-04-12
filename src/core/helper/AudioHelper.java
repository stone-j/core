package core.helper;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class AudioHelper {
	
	//https://stackoverflow.com/questions/2416935/how-to-play-wav-files-with-java
	public static void PlayAudioFile(File file)
	{
		try
		{
			final Clip clip = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));

			clip.addLineListener(new LineListener()
			{
				@Override
				public void update(LineEvent event)
				{
					if (event.getType() == LineEvent.Type.STOP)
						clip.close();
				}
			});

			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
		}
		catch (Exception exc)
		{
			exc.printStackTrace(System.out);
		}
	}
}
