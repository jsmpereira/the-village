package test.villagepeople.navigation;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.collection.IsCollectionContaining.*;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.junit.Test;

import explicitlib.geometry.Point;
import villagepeople.navigation.Map;
import villagepeople.navigation.Map.Cell;


public class MapTest {

    @Test
    public void diagonalPathTransversedCells() {
        Map map = new Map(10, 10);
        Set<Cell> cells = map.pathTransversedCells(new Point(0, 0), new Point(9.9, 9.9));
        
        assertThat(cells.size(), is(10));
        assertThat(cells, hasItem(map.cellAt(0, 0)));
        assertThat(cells, hasItem(map.cellAt(1, 1)));
        assertThat(cells, hasItem(map.cellAt(2, 2)));
        assertThat(cells, hasItem(map.cellAt(3, 3)));
        assertThat(cells, hasItem(map.cellAt(4, 4)));
        assertThat(cells, hasItem(map.cellAt(5, 5)));
        assertThat(cells, hasItem(map.cellAt(6, 6)));
        assertThat(cells, hasItem(map.cellAt(7, 7)));
        assertThat(cells, hasItem(map.cellAt(8, 8)));
        assertThat(cells, hasItem(map.cellAt(9, 9)));
                
        cells = map.pathTransversedCells(new Point(9.9, 9.9), new Point(0, 0));
        
        assertThat(cells.size(), is(10));
        assertThat(cells, hasItem(map.cellAt(0, 0)));
        assertThat(cells, hasItem(map.cellAt(1, 1)));
        assertThat(cells, hasItem(map.cellAt(2, 2)));
        assertThat(cells, hasItem(map.cellAt(3, 3)));
        assertThat(cells, hasItem(map.cellAt(4, 4)));
        assertThat(cells, hasItem(map.cellAt(5, 5)));
        assertThat(cells, hasItem(map.cellAt(6, 6)));
        assertThat(cells, hasItem(map.cellAt(7, 7)));
        assertThat(cells, hasItem(map.cellAt(8, 8)));
        assertThat(cells, hasItem(map.cellAt(9, 9)));
    }
    
    @Test
    public void horizontalPathTransversedCells() {
        Map map = new Map(10, 10);
        Set<Cell> cells = map.pathTransversedCells(new Point(0, 1.5), new Point(9.99, 1.5));
        
        assertThat(cells.size(), is(10));
        assertThat(cells, hasItem(map.cellAt(0, 1)));
        assertThat(cells, hasItem(map.cellAt(1, 1)));
        assertThat(cells, hasItem(map.cellAt(2, 1)));
        assertThat(cells, hasItem(map.cellAt(3, 1)));
        assertThat(cells, hasItem(map.cellAt(4, 1)));
        assertThat(cells, hasItem(map.cellAt(5, 1)));
        assertThat(cells, hasItem(map.cellAt(6, 1)));
        assertThat(cells, hasItem(map.cellAt(7, 1)));
        assertThat(cells, hasItem(map.cellAt(8, 1)));
        assertThat(cells, hasItem(map.cellAt(9, 1)));
        
        cells = map.pathTransversedCells(new Point(9.99, 1.5), new Point(0, 1.5));
        
        assertThat(cells.size(), is(10));
        assertThat(cells, hasItem(map.cellAt(0, 1)));
        assertThat(cells, hasItem(map.cellAt(1, 1)));
        assertThat(cells, hasItem(map.cellAt(2, 1)));
        assertThat(cells, hasItem(map.cellAt(3, 1)));
        assertThat(cells, hasItem(map.cellAt(4, 1)));
        assertThat(cells, hasItem(map.cellAt(5, 1)));
        assertThat(cells, hasItem(map.cellAt(6, 1)));
        assertThat(cells, hasItem(map.cellAt(7, 1)));
        assertThat(cells, hasItem(map.cellAt(8, 1)));
        assertThat(cells, hasItem(map.cellAt(9, 1)));
    }
    
    
    @Test
    public void verticalPathTransversedCells() {
        Map map = new Map(10, 10);
        Set<Cell> cells = map.pathTransversedCells(new Point(9.99, 0), new Point(9.99, 9.99));
        
        assertThat(cells.size(), is(10));
        assertThat(cells, hasItem(map.cellAt(9, 0)));
        assertThat(cells, hasItem(map.cellAt(9, 1)));
        assertThat(cells, hasItem(map.cellAt(9, 2)));
        assertThat(cells, hasItem(map.cellAt(9, 3)));
        assertThat(cells, hasItem(map.cellAt(9, 4)));
        assertThat(cells, hasItem(map.cellAt(9, 5)));
        assertThat(cells, hasItem(map.cellAt(9, 6)));
        assertThat(cells, hasItem(map.cellAt(9, 7)));
        assertThat(cells, hasItem(map.cellAt(9, 8)));
        assertThat(cells, hasItem(map.cellAt(9, 9)));
        
        cells = map.pathTransversedCells(new Point(9.99, 9.99), new Point(9.99, 0));
        
        assertThat(cells.size(), is(10));
        assertThat(cells, hasItem(map.cellAt(9, 0)));
        assertThat(cells, hasItem(map.cellAt(9, 1)));
        assertThat(cells, hasItem(map.cellAt(9, 2)));
        assertThat(cells, hasItem(map.cellAt(9, 3)));
        assertThat(cells, hasItem(map.cellAt(9, 4)));
        assertThat(cells, hasItem(map.cellAt(9, 5)));
        assertThat(cells, hasItem(map.cellAt(9, 6)));
        assertThat(cells, hasItem(map.cellAt(9, 7)));
        assertThat(cells, hasItem(map.cellAt(9, 8)));
        assertThat(cells, hasItem(map.cellAt(9, 9)));
    }
    
    
    @Test
    public void singlePathTransversedCells() {
        Map map = new Map(10, 10);
        Set<Cell> cells = map.pathTransversedCells(new Point(0.99, 0.6), new Point(0.1, 0.99));
        
        assertThat(cells.size(), is(1));
        assertThat(cells, hasItem(map.cellAt(0, 0)));
    }
    
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void outOfBoundsPathTransversedCells() {
        Map map = new Map(10, 10);
        Set<Cell> cells = map.pathTransversedCells(new Point(100, 100), new Point(0.1, 0.99));
    }
}
