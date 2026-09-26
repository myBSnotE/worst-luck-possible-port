package com.worstluckpossible.client;

import com.worstluckpossible.config.WorstLuckConfig;
import java.util.Arrays;
import java.util.Locale;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public final class WorstLuckConfigScreen extends Screen {
	private static final int CONTROL_WIDTH = 310;
	private final Screen parent;
	private final WorstLuckConfig config;
	private final boolean worldSettings;
	private final boolean editable;

	public WorstLuckConfigScreen(Screen parent, WorstLuckConfig config, boolean worldSettings, boolean editable) {
		super(Text.translatable("worstluck.config.title"));
		this.parent = parent;
		this.config = config;
		this.worldSettings = worldSettings;
		this.editable = editable;
	}

	@Override
	protected void init() {
		int x = (width - CONTROL_WIDTH) / 2;
		int y = 48;
		addEnum(x, y, "storm_frequency", config.stormFrequency, WorstLuckConfig.StormFrequency.values(),
				value -> config.stormFrequency = value);
		y += 24;
		addEnum(x, y, "lightning_targets", config.lightningTargets, WorstLuckConfig.LightningTargets.values(),
				value -> config.lightningTargets = value);
		y += 24;
		PercentSlider frequency = new PercentSlider(x, y, CONTROL_WIDTH, config.lightningFrequencyPercent);
		frequency.active = editable;
		frequency.setTooltip(Tooltip.of(Text.translatable("worstluck.config.lightning_frequency.tooltip")));
		addDrawableChild(frequency);
		y += 24;
		addEnum(x, y, "fishing", config.fishingMode, WorstLuckConfig.FishingMode.values(),
				value -> config.fishingMode = value);
		y += 24;
		addEnum(x, y, "fire", config.fireMode, WorstLuckConfig.FireMode.values(),
				value -> config.fireMode = value);
		y += 24;
		CyclingButtonWidget<Boolean> responsible = CyclingButtonWidget.onOffBuilder(config.responsibleMode)
				.tooltip(value -> Tooltip.of(Text.translatable("worstluck.config.responsible.tooltip")))
				.build(x, y, CONTROL_WIDTH, 20, Text.translatable("worstluck.config.responsible"),
						(button, value) -> {
							boolean previous = config.responsibleMode;
							if (value == previous || client == null) return;
							client.setScreen(new ConfirmScreen(confirmed -> {
								if (confirmed) {
									config.responsibleMode = value;
								} else {
									button.setValue(previous);
								}
								client.setScreen(this);
							}, Text.translatable("worstluck.config.responsible.confirm.title"),
									Text.translatable(value
											? "worstluck.config.responsible.confirm.enable"
											: "worstluck.config.responsible.confirm.disable")));
						});
		responsible.active = editable;
		addDrawableChild(responsible);

		int bottom = height - 28;
		ButtonWidget save = ButtonWidget.builder(Text.translatable("worstluck.config.save"), button -> {
			ClientConfigState.save(config, worldSettings);
			close();
		}).dimensions(width / 2 - 154, bottom, 150, 20).build();
		save.active = editable;
		if (!editable) save.setTooltip(Tooltip.of(Text.translatable("worstluck.config.read_only.tooltip")));
		addDrawableChild(save);
		addDrawableChild(ButtonWidget.builder(Text.translatable("gui.cancel"), button -> close())
				.dimensions(width / 2 + 4, bottom, 150, 20).build());
	}

	private <T extends Enum<T>> void addEnum(int x, int y, String key, T current, T[] values,
			java.util.function.Consumer<T> setter) {
		CyclingButtonWidget<T> widget = CyclingButtonWidget.builder(
				value -> Text.translatable(valueKey(key, value)), current)
				.values(Arrays.asList(values))
				.tooltip(value -> Tooltip.of(Text.translatable(valueKey(key, value) + ".tooltip")))
				.build(x, y, CONTROL_WIDTH, 20, Text.translatable("worstluck.config." + key),
						(button, value) -> setter.accept(value));
		widget.active = editable;
		if (!editable) widget.setTooltip(Tooltip.of(Text.translatable("worstluck.config.read_only.tooltip")));
		addDrawableChild(widget);
	}

	private static String valueKey(String option, Enum<?> value) {
		return "worstluck.config." + option + "." + value.name().toLowerCase(Locale.ROOT);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		// Screen#renderWithTooltip draws and blurs the background before calling this
		// method in 1.21.11. Calling renderBackground here would request a second blur
		// in the same frame and crash with "Can only blur once per frame".
		super.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 15, 0xFFFFFF);
		Text scope = Text.translatable(worldSettings
				? (editable ? "worstluck.config.scope.world" : "worstluck.config.scope.world_locked")
				: "worstluck.config.scope.defaults");
		context.drawCenteredTextWithShadow(textRenderer, scope, width / 2, 30,
				editable ? 0xA0A0A0 : 0xFF8080);
	}

	@Override
	public void close() {
		if (client != null) client.setScreen(parent);
	}

	private final class PercentSlider extends SliderWidget {
		private PercentSlider(int x, int y, int width, int percent) {
			super(x, y, width, 20, Text.empty(), (percent - 1) / 99.0D);
			updateMessage();
		}

		@Override protected void updateMessage() {
			setMessage(Text.translatable("worstluck.config.lightning_frequency", percent()));
		}

		@Override protected void applyValue() {
			config.lightningFrequencyPercent = percent();
		}

		private int percent() {
			return 1 + (int) Math.round(value * 99.0D);
		}
	}
}
