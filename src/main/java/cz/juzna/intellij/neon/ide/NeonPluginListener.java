package cz.juzna.intellij.neon.ide;

import com.intellij.ide.plugins.*;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.*;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.updateSettings.impl.pluginsAdvertisement.PluginsAdvertiser;
import com.jetbrains.php.PhpIndex;
import cz.juzna.intellij.neon.NeonBundle;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;

public class NeonPluginListener implements ProjectManagerListener {

	private static final @NotNull Logger LOG = Logger.getInstance(CommandProcessor.class);
	private static final @NotNull String NOTIFICATION_GROUP = "NEON Pro Group";
	private static final @NotNull String PLUGIN_PAGE_URL = "https://plugins.jetbrains.com/plugin/18387-neon-nette-support";
	private static final @NotNull String DONATE_PAGE_URL = "https://github.com/sponsors/mesour";

	@Override
	public void projectOpened(@NotNull Project project) {
		StartupManager.getInstance(project).registerPostStartupActivity((DumbAwareRunnable) () -> {
				try {
					Thread.sleep(60000);

					DumbService.getInstance(project).smartInvokeLater(() -> {
						PhpIndex phpIndex = PhpIndex.getInstance(project);
						if (phpIndex.getClassesByFQN("Nette\\DI\\Container").size() > 0) {
							neonProSuggestion(NeonBundle.message("notification.neonProSuggestion.message"));
						} else {
							neonDonateSuggestion(NeonBundle.message("notification.neonDonate.message"));
						}
					});

				} catch (InterruptedException| IndexNotReadyException e) {
					//do nothing
				}
		});
	}

	public static void neonDonateSuggestion(final @NotNull String message) {
		final Notification notification = new Notification(NOTIFICATION_GROUP, message, NotificationType.IDE_UPDATE);
		notification.addAction(new NotificationAction(NeonBundle.message("notification.neonDonate.button.donate")) {
			@Override
			public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
				openUrl(DONATE_PAGE_URL);
			}
		});

		showNotification(notification);
	}

	public static void neonProSuggestion(final @NotNull String message) {
		try {
			final Notification notification = new Notification(NOTIFICATION_GROUP, message, NotificationType.IDE_UPDATE);
			notification.addAction(new NotificationAction(NeonBundle.message("notification.neonProSuggestion.button.install")) {
				@Override
				public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
					PluginId pluginId = PluginId.getId("com.mesour.intellij.neon");
					PluginManagerCore.getPlugin(pluginId);
					PluginsAdvertiser.installAndEnable(
							null,
							new HashSet<PluginId>() {{ add(pluginId); }},
							true,
							() -> PluginManager.disablePlugin("cz.juzna.intellij.neon")
					);
				}
			});
			notification.addAction(new NotificationAction(NeonBundle.message("notification.neonProSuggestion.button.seeMore")) {
				@Override
				public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
					openUrl(PLUGIN_PAGE_URL);
				}
			});
			final Application app = ApplicationManager.getApplication();
			if (!app.isDisposed()) {
				app.getMessageBus().syncPublisher(Notifications.TOPIC).notify(notification);
			}

		} catch (IllegalMonitorStateException e) {
			LOG.error(e);
		}
	}

	private static void showNotification(final Notification notification) {
		final Application app = ApplicationManager.getApplication();
		if (!app.isDisposed()) {
			app.getMessageBus().syncPublisher(Notifications.TOPIC).notify(notification);
		}
	}

	private static void openUrl(final String url) {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				try {
					URI uri = new URI(url);
					desktop.browse(uri);
				} catch (URISyntaxException | IOException ignored) {
				}
			}
		}
	}

}
