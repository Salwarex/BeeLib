package ru.waxera.beeLib.utils.particles;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

@Deprecated
public class LineParticleFigure extends ParticleFigure {

    private LineParticleType type;

    public LineParticleFigure(Particle particle, Location location, LineParticleType type,
                              int segments, double step, double x_angle, double y_angle, double z_angle,
                              int count, int period, Color color){
        super(particle, location, count, 0, 0, 0, 0, x_angle, y_angle, z_angle, period, segments, step, color);
        this.type = type;
    }

    public void setOffset(double offsetX, double offsetY, double offsetZ, double extra){
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.extra = extra;
    }

    @Override
    public void execute() {
        switch (type) {
            case UPPER_DOWN:
                draw(this::drawLineStep, -segments, 0);
                break;
            case DOWN_UPPER:
                draw(this::drawLineStep, 0, segments);
                break;
            case CENTER:
                draw(this::drawLineStep, -segments, segments);
                break;
        }
    }

    private void drawLineStep(int i) {
        if (i == 0 && type != LineParticleType.CENTER) return;

        Location particleLoc = location.clone().add(0, i * step, 0);
        Location rotatedLoc = rotateLocation(particleLoc);

        displayParticle(rotatedLoc);
    }
}

enum LineParticleType{
    UPPER_DOWN,
    DOWN_UPPER,
    CENTER
}
