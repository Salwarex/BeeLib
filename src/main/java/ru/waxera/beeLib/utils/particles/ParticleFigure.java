package ru.waxera.beeLib.utils.particles;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.waxera.beeLib.BeeLib;

@Deprecated
public abstract class ParticleFigure {
    protected Particle particle;
    protected Location location;
    protected int count = 1;
    protected double offsetX = 0;
    protected double offsetY = 0;
    protected double offsetZ = 0;
    protected double extra = 0;
    protected double x_angle;
    protected double y_angle;
    protected double z_angle;
    protected int period;
    protected int segments;
    protected double step;

    private BukkitTask task;

    protected ParticleFigure(Particle particle,
                             Location location,
                             int count,
                             double offsetX,
                             double offsetY,
                             double offsetZ,
                             double extra,
                             double x_angle,
                             double y_angle,
                             double z_angle,
                             int period,
                             int segments,
                             double step){
        this.particle = particle;
        this.location = location;
        this.count = count;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.extra = extra;
        this.x_angle = x_angle;
        this.y_angle = y_angle;
        this.z_angle = z_angle;
        this.period = period;
        this.segments = segments;
        this.step = step;
    }



    protected Location rotateLocation(Location point) {
        double xAngle = Math.toRadians(x_angle);
        double yAngle = Math.toRadians(y_angle);
        double zAngle = Math.toRadians(z_angle);

        double x = point.getX() - location.getX();
        double y = point.getY() - location.getY();
        double z = point.getZ() - location.getZ();

        double x1 = x;
        double y1 = y * Math.cos(xAngle) - z * Math.sin(xAngle);
        double z1 = y * Math.sin(xAngle) + z * Math.cos(xAngle);

        double x2 = x1 * Math.cos(zAngle) - y1 * Math.sin(zAngle);
        double y2 = x1 * Math.sin(zAngle) + y1 * Math.cos(zAngle);
        double z2 = z1;

        double x3 = x2;
        double z3;
        double y3 = y2;

        if (Math.abs(y_angle) > 0.001) {
            x3 = x2 * Math.cos(yAngle) + z2 * Math.sin(yAngle);
            z3 = -x2 * Math.sin(yAngle) + z2 * Math.cos(yAngle);
        } else {
            z3 = z2;
        }

        return location.clone().add(x3, y3, z3);
    }

    protected void draw(DrawAlgorithm algorithm, int start, int end){
        task = new BukkitRunnable(){
            private int current = start;

            @Override
            public void run() {
                if (current > end) {
                    this.cancel();
                    return;
                }

                algorithm.run(current);

                current++;
            }
        }.runTaskTimer(BeeLib.getInstance(), 0, this.period);
    }

    protected void displayParticle(Location location) {
        World world = location.getWorld();
        if (world == null) return;

        world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra);
    }

    public void execute(){}
}
