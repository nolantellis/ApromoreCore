/*
 * This file is part of "Apromore Core".
 *
 * Copyright (C) 2018-2020 The University of Melbourne.
 *
 * "Apromore Core" is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * "Apromore Core" is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package org.apromore.logman.attribute.graph;

import org.apromore.logman.ALog;
import org.apromore.logman.Constants;
import org.apromore.logman.DataSetup;
import org.apromore.logman.attribute.log.AttributeLog;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.eclipse.collections.impl.factory.primitive.IntSets;
import org.junit.Assert;
import org.junit.Test;

public class AttributeLogGraphTest extends DataSetup {

    @Test
    public void test_OneTraceAndCompleteEvents() throws InvalidArcException {
        ALog log = new ALog(readLogWithOneTraceAndCompleteEvents());
        AttributeLog attLog = new AttributeLog(log, log.getAttributeStore().getStandardEventConceptName());
        
        AttributeLogGraph graph = attLog.getGraphView().getLogGraph();
        graph.buildSubGraphs(attLog.getAttribute(), MeasureType.FREQUENCY, MeasureAggregation.TOTAL, false, false);
        
        Assert.assertEquals(IntSets.mutable.of(0,1,2,3,4,5), graph.getOriginalNodes());
        Assert.assertEquals(IntSets.mutable.of(0,1,2,3,4,5), graph.getNodes());
        Assert.assertEquals(IntSets.mutable.of(0,1,2,8,12,15,17,20,24), graph.getOriginalArcs());
        Assert.assertEquals(IntSets.mutable.of(0,1,2,8,12,15,17,20,24), graph.getArcs());
        Assert.assertEquals(6, graph.cloneNodeBitMask().cardinality());
        Assert.assertEquals(9, graph.cloneArcBitMask().cardinality());
        Assert.assertEquals(4, graph.getSourceNode());
        Assert.assertEquals(5, graph.getSinkNode());
        
        Assert.assertEquals("a", graph.getNodeName(0));
        Assert.assertEquals("b", graph.getNodeName(1));
        Assert.assertEquals("c", graph.getNodeName(2));
        Assert.assertEquals("d", graph.getNodeName(3));
        Assert.assertEquals(Constants.START_NAME, graph.getNodeName(4));
        Assert.assertEquals(Constants.END_NAME, graph.getNodeName(5));
        
        Assert.assertEquals(IntLists.mutable.of(1,4,5,3,0,2), graph.getSortedNodes());
        
        Assert.assertEquals(IntSets.mutable.empty(), graph.getOutgoingArcs(100)); // non-existent node
        Assert.assertEquals(IntSets.mutable.empty(), graph.getIncomingArcs(100));
        
        Assert.assertEquals(IntSets.mutable.of(0,1,2), graph.getOutgoingArcs(0)); //a
        Assert.assertEquals(IntSets.mutable.of(0,12,24), graph.getIncomingArcs(0)); 
        
        Assert.assertEquals(IntSets.mutable.of(8), graph.getOutgoingArcs(1)); //b
        Assert.assertEquals(IntSets.mutable.of(1), graph.getIncomingArcs(1)); 
        
        Assert.assertEquals(IntSets.mutable.of(12,15,17), graph.getOutgoingArcs(2)); //c
        Assert.assertEquals(IntSets.mutable.of(2,8,20), graph.getIncomingArcs(2)); 
        
        Assert.assertEquals(IntSets.mutable.of(20), graph.getOutgoingArcs(3)); //d
        Assert.assertEquals(IntSets.mutable.of(15), graph.getIncomingArcs(3)); 
        
        Assert.assertEquals(IntSets.mutable.of(24), graph.getOutgoingArcs(4)); //source -1
        Assert.assertEquals(IntSets.mutable.empty(), graph.getIncomingArcs(4)); 
        
        Assert.assertEquals(IntSets.mutable.empty(), graph.getOutgoingArcs(5)); //sink -2
        Assert.assertEquals(IntSets.mutable.of(17), graph.getIncomingArcs(5)); 
        
        Assert.assertEquals(0, graph.getArc(0, 0)); //a->a
        Assert.assertEquals(1, graph.getArc(0, 1)); //a->b
        Assert.assertEquals(2, graph.getArc(0, 2)); //a->c
        Assert.assertEquals(8, graph.getArc(1, 2)); //b->c
        Assert.assertEquals(12, graph.getArc(2, 0)); //c->a
        Assert.assertEquals(15, graph.getArc(2, 3)); //c->d
        Assert.assertEquals(20, graph.getArc(3, 2)); //d->c
        Assert.assertEquals(24, graph.getArc(4, 0)); //-1 -> a
        Assert.assertEquals(17, graph.getArc(2, 5)); //c->-2
        
        Assert.assertEquals(IntLists.mutable.of(1,2,8,12,17,24,0,15,20), graph.getSortedArcs());
        
        Assert.assertEquals(4, graph.getNodeWeight(0, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(4, graph.getNodeWeight(2, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(2, graph.getNodeWeight(3, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(4, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(5, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.0);
        
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.CASES),0.0);
        
        // 0,1,2,8,12,15,17,20,24
        Assert.assertEquals(2, graph.getArcWeight(0, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(1, graph.getArcWeight(1, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(1, graph.getArcWeight(2, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(1, graph.getArcWeight(8, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(1, graph.getArcWeight(12, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(2, graph.getArcWeight(15, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(1, graph.getArcWeight(17, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(2, graph.getArcWeight(20, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(1, graph.getArcWeight(24, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        
        Assert.assertEquals(1, graph.getArcWeight(0, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(1, graph.getArcWeight(1, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(1, graph.getArcWeight(2, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(1, graph.getArcWeight(8, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(1, graph.getArcWeight(12, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(1, graph.getArcWeight(15, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(1, graph.getArcWeight(17, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(1, graph.getArcWeight(20, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(1, graph.getArcWeight(24, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        
        Assert.assertEquals(2, graph.getArcWeight(0, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(1, graph.getArcWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(1, graph.getArcWeight(2, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(1, graph.getArcWeight(8, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(1, graph.getArcWeight(12, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(2, graph.getArcWeight(15, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(1, graph.getArcWeight(17, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(2, graph.getArcWeight(20, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(1, graph.getArcWeight(24, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        
        Assert.assertEquals(2, graph.getArcWeight(0, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getArcWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getArcWeight(2, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getArcWeight(8, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getArcWeight(12, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(2, graph.getArcWeight(15, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getArcWeight(17, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(2, graph.getArcWeight(20, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getArcWeight(24, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        
        Assert.assertEquals(2, graph.getArcWeight(0, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(1, graph.getArcWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(1, graph.getArcWeight(2, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(1, graph.getArcWeight(8, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(1, graph.getArcWeight(12, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(2, graph.getArcWeight(15, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(1, graph.getArcWeight(17, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(2, graph.getArcWeight(20, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(1, graph.getArcWeight(24, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.0);
        
        Assert.assertEquals(180000, graph.getArcWeight(0, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(180000, graph.getArcWeight(1, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(600000, graph.getArcWeight(2, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(240000, graph.getArcWeight(8, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(540000, graph.getArcWeight(12, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(720000, graph.getArcWeight(15, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(0, graph.getArcWeight(17, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(840000, graph.getArcWeight(20, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(0, graph.getArcWeight(24, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        
        Assert.assertEquals(60000, graph.getArcWeight(0, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(180000, graph.getArcWeight(1, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(600000, graph.getArcWeight(2, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(240000, graph.getArcWeight(8, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(540000, graph.getArcWeight(12, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(300000, graph.getArcWeight(15, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(0, graph.getArcWeight(17, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(360000, graph.getArcWeight(20, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(0, graph.getArcWeight(24, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        
        Assert.assertEquals(120000, graph.getArcWeight(0, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(180000, graph.getArcWeight(1, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(600000, graph.getArcWeight(2, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(240000, graph.getArcWeight(8, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(540000, graph.getArcWeight(12, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(420000, graph.getArcWeight(15, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(0, graph.getArcWeight(17, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(480000, graph.getArcWeight(20, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(0, graph.getArcWeight(24, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        
        Assert.assertEquals(90000, graph.getArcWeight(0, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(180000, graph.getArcWeight(1, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(600000, graph.getArcWeight(2, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(240000, graph.getArcWeight(8, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(540000, graph.getArcWeight(12, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(360000, graph.getArcWeight(15, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(0, graph.getArcWeight(17, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(420000, graph.getArcWeight(20, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(0, graph.getArcWeight(24, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        
        
        //Subgraphs
        
        Assert.assertEquals(IntSets.mutable.of(0, 1, 2, 3, 4, 5), graph.getSubGraphs().get(0).getNodes());
        Assert.assertEquals(IntSets.mutable.of(0, 1, 2, 8, 12, 15, 17, 20, 24), graph.getSubGraphs().get(0).getArcs());
        
        Assert.assertEquals(IntSets.mutable.of(0, 2, 3, 4, 5), graph.getSubGraphs().get(1).getNodes());
        Assert.assertEquals(IntSets.mutable.of(0, 2, 12, 15, 17, 20, 24), graph.getSubGraphs().get(1).getArcs());
        
        Assert.assertEquals(IntSets.mutable.of(0, 2, 4, 5), graph.getSubGraphs().get(2).getNodes());
        Assert.assertEquals(IntSets.mutable.of(0, 2, 12, 17, 24), graph.getSubGraphs().get(2).getArcs());
        
        AttributeGraph nodeBasedGraph2 = graph.getSubGraphs().get(2);
        Assert.assertEquals(IntSets.mutable.of(0, 2, 4, 5), nodeBasedGraph2.getSubGraphs().get(0).getNodes());
        Assert.assertEquals(IntSets.mutable.of(0, 2, 12, 17, 24), nodeBasedGraph2.getSubGraphs().get(0).getArcs());
        Assert.assertEquals(IntSets.mutable.of(0, 2, 4, 5), nodeBasedGraph2.getSubGraphs().get(1).getNodes());
        Assert.assertEquals(IntSets.mutable.of(0, 2, 17, 24), nodeBasedGraph2.getSubGraphs().get(1).getArcs());
        Assert.assertEquals(IntSets.mutable.of(0, 2, 4, 5), nodeBasedGraph2.getSubGraphs().get(2).getNodes());
        Assert.assertEquals(IntSets.mutable.of(2, 17, 24), nodeBasedGraph2.getSubGraphs().get(2).getArcs());  
        
        AttributeGraph nodeBasedGraph1 = graph.getSubGraphs().get(1);
        Assert.assertEquals(IntSets.mutable.of(0, 2, 3, 4, 5), nodeBasedGraph1.getSubGraphs().get(0).getNodes());
        Assert.assertEquals(IntSets.mutable.of(0, 2, 12, 15, 17, 20, 24), nodeBasedGraph1.getSubGraphs().get(0).getArcs());
        Assert.assertEquals(IntSets.mutable.of(0, 2, 3, 4, 5), nodeBasedGraph1.getSubGraphs().get(1).getNodes());
        Assert.assertEquals(IntSets.mutable.of(0, 2, 15, 17, 20, 24), nodeBasedGraph1.getSubGraphs().get(1).getArcs());
        Assert.assertEquals(IntSets.mutable.of(0, 2, 3, 4, 5), nodeBasedGraph1.getSubGraphs().get(2).getNodes());
        Assert.assertEquals(IntSets.mutable.of(2, 15, 17, 20, 24), nodeBasedGraph1.getSubGraphs().get(2).getArcs());  
        
        AttributeGraph nodeBasedGraph0 = graph.getSubGraphs().get(0);
        Assert.assertEquals(IntSets.mutable.of(0, 1, 2, 3, 4, 5), nodeBasedGraph0.getSubGraphs().get(0).getNodes());
        Assert.assertEquals(IntSets.mutable.of(0, 1, 2, 8, 12, 15, 17, 20, 24), nodeBasedGraph0.getSubGraphs().get(0).getArcs());  
        Assert.assertEquals(IntSets.mutable.of(0, 1, 2, 3, 4, 5), nodeBasedGraph0.getSubGraphs().get(1).getNodes());
        Assert.assertEquals(IntSets.mutable.of(0, 1, 8, 12, 15, 17, 20, 24), nodeBasedGraph0.getSubGraphs().get(1).getArcs());     
        Assert.assertEquals(IntSets.mutable.of(0, 1, 2, 3, 4, 5), nodeBasedGraph0.getSubGraphs().get(2).getNodes());
        Assert.assertEquals(IntSets.mutable.of(0, 1, 8, 15, 17, 20, 24), nodeBasedGraph0.getSubGraphs().get(2).getArcs());           
        Assert.assertEquals(IntSets.mutable.of(0, 1, 2, 3, 4, 5), nodeBasedGraph0.getSubGraphs().get(3).getNodes());
        Assert.assertEquals(IntSets.mutable.of(1, 8, 15, 17, 20, 24), nodeBasedGraph0.getSubGraphs().get(3).getArcs());  

    }
    
    @Test 
    public void test_Exception() {
        ALog log = new ALog(readLogWithOneTraceAndCompleteEvents());
        AttributeLog attLog = new AttributeLog(log, log.getAttributeStore().getStandardEventConceptName());
        AttributeLogGraph graph = attLog.getGraphView().getLogGraph();
        graph.buildSubGraphs(attLog.getAttribute(), MeasureType.FREQUENCY, MeasureAggregation.TOTAL, false, false);
        
        try {
            graph.removeNode(100);
            Assert.fail("InvalidNodeException or InvalidArcException does not happen");
        } catch (InvalidNodeException | InvalidArcException e1) {
            // TODO Auto-generated catch block
        }
        
        try {
            graph.addNode(100);
            Assert.fail("InvalidNodeException or InvalidArcException does not happen");
        } catch (InvalidNodeException e) {
            // TODO Auto-generated catch block
        }
        
        
        try {
            int arc1 = graph.getArc(0, 3);
            Assert.fail("Expected Exception does not happen");
        } catch (InvalidArcException e) {
            //
        }
        
        
        
        try {
            int arc2 = graph.getArc(100, 0);
            Assert.fail("Expected Exception does not happen");
        } catch (InvalidArcException e) {
            // TODO Auto-generated catch block
            
        }
    }
    
    @Test 
    public void test_Remove_Add_Node() {
        ALog log = new ALog(readLogWithOneTraceAndCompleteEvents());
        AttributeLog attLog = new AttributeLog(log, log.getAttributeStore().getStandardEventConceptName());
        AttributeLogGraph graph = attLog.getGraphView().getLogGraph();
        graph.buildSubGraphs(attLog.getAttribute(), MeasureType.FREQUENCY, MeasureAggregation.TOTAL, false, false);
        
        try {
            graph.removeNode(3);
            
            Assert.assertEquals(false, graph.containNode(3));
            Assert.assertEquals(false, graph.containArc(15));
            Assert.assertEquals(false, graph.containArc(20));
            
            Assert.assertEquals(IntSets.mutable.of(0,1,2,3,4,5), graph.getOriginalNodes());
            Assert.assertEquals(IntSets.mutable.of(0,1,2,4,5), graph.getNodes());
            Assert.assertEquals(IntSets.mutable.of(0,1,2,8,12,15,17,20,24), graph.getOriginalArcs());
            Assert.assertEquals(IntSets.mutable.of(0,1,2,8,12,17,24), graph.getArcs());
            Assert.assertEquals(5, graph.cloneNodeBitMask().cardinality());
            Assert.assertEquals(7, graph.cloneArcBitMask().cardinality());
            Assert.assertEquals(4, graph.getSourceNode());
            Assert.assertEquals(5, graph.getSinkNode());
            
            Assert.assertEquals(IntSets.mutable.empty(), graph.getOutgoingArcs(3)); // non-existent node
            Assert.assertEquals(IntSets.mutable.empty(), graph.getIncomingArcs(3));
            
            // Original arcs still exist
            Assert.assertEquals(IntSets.mutable.of(20), graph.getOutgoingOriginalArcs(3)); //d
            Assert.assertEquals(IntSets.mutable.of(15), graph.getIncomingOriginalArcs(3)); 
            
        } catch (InvalidNodeException | InvalidArcException e) {
            Assert.fail("Unexpected exception happened!");
        }
        
        try {
            graph.getArc(2, 3);
            graph.getArc(3, 2);
            Assert.fail("InvalidArcException did not happen!");
        } catch (InvalidArcException e) {
            // TODO Auto-generated catch block
        }
        
        try {
            Assert.assertEquals(15, graph.getOriginalArc(2, 3)); //c->d
            Assert.assertEquals(20, graph.getOriginalArc(3, 2)); //d->c
        } catch (InvalidArcException e) {
            Assert.fail("InvalidArcException is NOT expected!");
        } 
        
        // Still can get weights of the removed node and arcs since they exist in the original graph
        Assert.assertEquals(2, graph.getNodeWeight(3, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(2, graph.getArcWeight(15, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(2, graph.getArcWeight(20, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        
        // Add invalid node: node not exist in the original graph
        try {
            graph.addNode(100);
            Assert.fail("InvalidNodeException is expected but DID NOT happen!");
        } catch (InvalidNodeException e) {
            // TODO Auto-generated catch block
        }
        
        // Add invalid node: node already exists in the current graph
        try {
            graph.addNode(1);
            Assert.fail("InvalidNodeException is expected but DID NOT happen!");
        } catch (InvalidNodeException e) {
            // TODO Auto-generated catch block
        }
        
        // Re-Add valid node
        try {
            graph.addNode(3);
            
            Assert.assertEquals(IntSets.mutable.of(0,1,2,3,4,5), graph.getNodes());
            Assert.assertEquals(IntSets.mutable.of(0,1,2,8,12,15,17,20,24), graph.getArcs());
            Assert.assertEquals(6, graph.cloneNodeBitMask().cardinality());
            Assert.assertEquals(9, graph.cloneArcBitMask().cardinality());
            
            Assert.assertEquals(true, graph.containNode(3));
            Assert.assertEquals(true, graph.containArc(15));
            Assert.assertEquals(true, graph.containArc(20));
            
            Assert.assertEquals(IntSets.mutable.of(20), graph.getOutgoingArcs(3)); //d
            Assert.assertEquals(IntSets.mutable.of(15), graph.getIncomingArcs(3)); 
            
            // Check the weight of adjacent arcs to the re-added node that they are unchanged
            Assert.assertEquals(2, graph.getNodeWeight(3, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
            Assert.assertEquals(2, graph.getArcWeight(15, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
            Assert.assertEquals(2, graph.getArcWeight(20, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
            
        } catch (InvalidNodeException e) {
            // TODO Auto-generated catch block
            Assert.fail("InvalidNodeException is unexpected but DID happen!");
        }
    }
    
    @Test 
    public void test_Remove_Add_Arc() {
        ALog log = new ALog(readLogWithOneTraceAndCompleteEvents());
        AttributeLog attLog = new AttributeLog(log, log.getAttributeStore().getStandardEventConceptName());
        AttributeLogGraph graph = attLog.getGraphView().getLogGraph();
        graph.buildSubGraphs(attLog.getAttribute(), MeasureType.FREQUENCY, MeasureAggregation.TOTAL, false, false);
        
        graph.removeArc(8);
        
        Assert.assertEquals(false, graph.containArc(8));
        Assert.assertEquals(IntSets.mutable.of(0,1,2,8,12,15,17,20,24), graph.getOriginalArcs());
        Assert.assertEquals(IntSets.mutable.of(0,1,2,12,15,17,20,24), graph.getArcs());
        Assert.assertEquals(8, graph.cloneArcBitMask().cardinality());
        
        // Arc should not exist any more.
        try {
            graph.getArc(1, 2);
            Assert.fail("InvalidArcException is expected to happen but DID NOT happen");
        } catch (InvalidArcException e) {
            // TODO Auto-generated catch block
        }
        
        
        // Original arc still exists
        try {
            Assert.assertEquals(8, graph.getOriginalArc(1, 2)); 
        } catch (InvalidArcException e) {
            Assert.fail("InvalidArcException is NOT expected!");
        } 
        
        // Unchanged weight
        Assert.assertEquals(1, graph.getArcWeight(8, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(240000, graph.getArcWeight(8, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        
        
        // Add an invalid arc (already exist)
        try {
            graph.addArc(0);
            Assert.fail("InvalidArcException is expected but DID NOT happen!");
        } catch (InvalidArcException e) {
        }
        
        // Add an invalid arc: not exist in the original graph
        try {
            graph.addArc(100);
            Assert.fail("InvalidArcException is expected but DID NOT happen!");
        } catch (InvalidArcException e) {
        }
        
        
        // Add a valid arc: arc exists in the original graph but not in the current graph
        try {
            graph.addArc(8);
            
            Assert.assertEquals(true, graph.containArc(8));
            Assert.assertEquals(IntSets.mutable.of(0,1,2,8,12,15,17,20,24), graph.getOriginalArcs());
            Assert.assertEquals(IntSets.mutable.of(0,1,2,8,12,15,17,20,24), graph.getArcs());
            Assert.assertEquals(9, graph.cloneArcBitMask().cardinality());
            
         // Unchanged weight
            Assert.assertEquals(1, graph.getArcWeight(8, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
            Assert.assertEquals(240000, graph.getArcWeight(8, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
            
        } catch (InvalidArcException e) {
            Assert.fail("InvalidArcException is unexpected but DID happen!");
        }
    }
    
    
    @Test
    public void test_LogWithCompleteEventsOnly() throws InvalidArcException {
        ALog log = new ALog(readLogWithCompleteEventsOnly());
        AttributeLog attLog = new AttributeLog(log, log.getAttributeStore().getStandardEventConceptName());
        
        AttributeLogGraph graph = attLog.getGraphView().getLogGraph();
        graph.buildSubGraphs(attLog.getAttribute(), MeasureType.FREQUENCY, MeasureAggregation.TOTAL, false, false);
        
        Assert.assertEquals(IntSets.mutable.of(0,1,2,3,4,5,6), graph.getOriginalNodes());
        Assert.assertEquals(IntSets.mutable.of(0,1,2,3,4,5,6), graph.getNodes());
        Assert.assertEquals(IntSets.mutable.of(1,3,4,9,20,23,25,30,31,35), graph.getOriginalArcs());
        Assert.assertEquals(IntSets.mutable.of(1,3,4,9,20,23,25,30,31,35), graph.getArcs());
        Assert.assertEquals(7, graph.cloneNodeBitMask().cardinality());
        Assert.assertEquals(10, graph.cloneArcBitMask().cardinality());
        Assert.assertEquals(5, graph.getSourceNode());
        Assert.assertEquals(6, graph.getSinkNode());
        
        Assert.assertEquals("a", graph.getNodeName(0));
        Assert.assertEquals("e", graph.getNodeName(1));
        Assert.assertEquals("d", graph.getNodeName(2));
        Assert.assertEquals("c", graph.getNodeName(3));
        Assert.assertEquals("b", graph.getNodeName(4));
        Assert.assertEquals("|>", graph.getNodeName(5));
        Assert.assertEquals("[]", graph.getNodeName(6));
        Assert.assertEquals(Constants.START_NAME, graph.getNodeName(5));
        Assert.assertEquals(Constants.END_NAME, graph.getNodeName(6));
        
        Assert.assertEquals(IntSets.mutable.empty(), graph.getOutgoingArcs(100)); // non-existent node
        Assert.assertEquals(IntSets.mutable.empty(), graph.getIncomingArcs(100));
        
        Assert.assertEquals(IntSets.mutable.of(1,3,4), graph.getOutgoingArcs(0)); //a
        Assert.assertEquals(IntSets.mutable.of(35), graph.getIncomingArcs(0)); 
        
        Assert.assertEquals(IntSets.mutable.of(9), graph.getOutgoingArcs(1)); //b
        Assert.assertEquals(IntSets.mutable.of(1), graph.getIncomingArcs(1)); 
        
        Assert.assertEquals(IntSets.mutable.of(20), graph.getOutgoingArcs(2)); //c
        Assert.assertEquals(IntSets.mutable.of(9,23,30), graph.getIncomingArcs(2)); 
        
        Assert.assertEquals(IntSets.mutable.of(23,25), graph.getOutgoingArcs(3)); //d
        Assert.assertEquals(IntSets.mutable.of(3,31), graph.getIncomingArcs(3)); 
        
        Assert.assertEquals(IntSets.mutable.of(30,31), graph.getOutgoingArcs(4)); 
        Assert.assertEquals(IntSets.mutable.of(4,25), graph.getIncomingArcs(4)); 
        
        Assert.assertEquals(IntSets.mutable.of(35), graph.getOutgoingArcs(5)); 
        Assert.assertEquals(IntSets.mutable.empty(), graph.getIncomingArcs(5)); 
        
        Assert.assertEquals(IntSets.mutable.empty(), graph.getOutgoingArcs(6)); 
        Assert.assertEquals(IntSets.mutable.of(20), graph.getIncomingArcs(6));
        
        Assert.assertEquals(1, graph.getArc(0, 1)); //a->a
        Assert.assertEquals(3, graph.getArc(0, 3)); //a->b
        Assert.assertEquals(4, graph.getArc(0, 4)); //a->c
        Assert.assertEquals(9, graph.getArc(1, 2)); //b->c
        Assert.assertEquals(23, graph.getArc(3, 2)); //c->a
        Assert.assertEquals(25, graph.getArc(3, 4)); //c->d
        Assert.assertEquals(30, graph.getArc(4, 2)); //d->c
        Assert.assertEquals(31, graph.getArc(4, 3)); //-1 -> a
        Assert.assertEquals(35, graph.getArc(5,0)); //c->-2
        
        Assert.assertEquals(6, graph.getNodeWeight(0, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(6, graph.getNodeWeight(2, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(5, graph.getNodeWeight(3, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(5, graph.getNodeWeight(4, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(6, graph.getNodeWeight(5, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(6, graph.getNodeWeight(6, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        
        Assert.assertEquals(IntLists.mutable.of(1,3,4,0,2,5,6), graph.getSortedNodes());
     
        Assert.assertEquals(6, graph.getNodeWeight(0, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(6, graph.getNodeWeight(2, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(5, graph.getNodeWeight(3, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(5, graph.getNodeWeight(4, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(6, graph.getNodeWeight(5, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(6, graph.getNodeWeight(6, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        
        Assert.assertEquals(1, graph.getNodeWeight(0, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(2, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(3, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(4, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(5, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(6, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        
        Assert.assertEquals(1, graph.getNodeWeight(0, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(2, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(3, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(4, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(5, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getNodeWeight(6, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        
        Assert.assertEquals(1, graph.getNodeWeight(0, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        Assert.assertEquals(0.16666, graph.getNodeWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        Assert.assertEquals(1, graph.getNodeWeight(2, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        Assert.assertEquals(0.83333, graph.getNodeWeight(3, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        Assert.assertEquals(0.83333, graph.getNodeWeight(4, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        Assert.assertEquals(1, graph.getNodeWeight(5, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        Assert.assertEquals(1, graph.getNodeWeight(6, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(0, graph.getNodeWeight(0, MeasureType.DURATION, MeasureAggregation.CASES),0.0);
        
        // 1,3,4,9,20,23,25,30,31,35
        Assert.assertEquals(1, graph.getArcWeight(1, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(2, graph.getArcWeight(3, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(3, graph.getArcWeight(4, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(1, graph.getArcWeight(9, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(6, graph.getArcWeight(20, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(3, graph.getArcWeight(23, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(2, graph.getArcWeight(25, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(2, graph.getArcWeight(30, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(3, graph.getArcWeight(31, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(6, graph.getArcWeight(35, MeasureType.FREQUENCY, MeasureAggregation.TOTAL),0.0);     
        
        Assert.assertEquals(IntLists.mutable.of(1,9,3,25,30,4,23,31,20,35), graph.getSortedArcs());

        Assert.assertEquals(1, graph.getArcWeight(1, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(2, graph.getArcWeight(3, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(3, graph.getArcWeight(4, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(1, graph.getArcWeight(9, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(6, graph.getArcWeight(20, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(3, graph.getArcWeight(23, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(2, graph.getArcWeight(25, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(2, graph.getArcWeight(30, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(3, graph.getArcWeight(31, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0);
        Assert.assertEquals(6, graph.getArcWeight(35, MeasureType.FREQUENCY, MeasureAggregation.CASES),0.0); 
        
        Assert.assertEquals(0, graph.getArcWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(0, graph.getArcWeight(3, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(0, graph.getArcWeight(4, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(0, graph.getArcWeight(9, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(1, graph.getArcWeight(20, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(0, graph.getArcWeight(23, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(0, graph.getArcWeight(25, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(0, graph.getArcWeight(30, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(0, graph.getArcWeight(31, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(1, graph.getArcWeight(35, MeasureType.FREQUENCY, MeasureAggregation.MIN),0.0);  
        
        Assert.assertEquals(1, graph.getArcWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getArcWeight(3, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getArcWeight(4, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getArcWeight(9, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getArcWeight(20, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getArcWeight(23, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getArcWeight(25, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getArcWeight(30, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getArcWeight(31, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(1, graph.getArcWeight(35, MeasureType.FREQUENCY, MeasureAggregation.MAX),0.0);
        
        Assert.assertEquals(0.16666, graph.getArcWeight(1, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        Assert.assertEquals(0.33333, graph.getArcWeight(3, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        Assert.assertEquals(0.5, graph.getArcWeight(4, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        Assert.assertEquals(0.16666, graph.getArcWeight(9, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        Assert.assertEquals(1, graph.getArcWeight(20, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        Assert.assertEquals(0.5, graph.getArcWeight(23, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        Assert.assertEquals(0.33333, graph.getArcWeight(25, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        Assert.assertEquals(0.33333, graph.getArcWeight(30, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        Assert.assertEquals(0.5, graph.getArcWeight(31, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        Assert.assertEquals(1, graph.getArcWeight(35, MeasureType.FREQUENCY, MeasureAggregation.MEAN),0.001);
        
        Assert.assertEquals(60000, graph.getArcWeight(1, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(120000, graph.getArcWeight(3, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(180000, graph.getArcWeight(4, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(9, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(0, graph.getArcWeight(20, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(180000, graph.getArcWeight(23, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(180000, graph.getArcWeight(25, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(120000, graph.getArcWeight(30, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(180000, graph.getArcWeight(31, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        Assert.assertEquals(0, graph.getArcWeight(35, MeasureType.DURATION, MeasureAggregation.TOTAL),0.0);
        
        Assert.assertEquals(60000, graph.getArcWeight(1, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(3, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(4, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(9, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(0, graph.getArcWeight(20, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(23, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(25, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(30, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(31, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        Assert.assertEquals(0, graph.getArcWeight(35, MeasureType.DURATION, MeasureAggregation.MIN),0.0);
        
        Assert.assertEquals(60000, graph.getArcWeight(1, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(3, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(4, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(9, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(0, graph.getArcWeight(20, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(23, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(120000, graph.getArcWeight(25, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(30, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(31, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        Assert.assertEquals(0, graph.getArcWeight(35, MeasureType.DURATION, MeasureAggregation.MAX),0.0);
        
        Assert.assertEquals(60000, graph.getArcWeight(1, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(3, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(4, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(9, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(0, graph.getArcWeight(20, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(23, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(90000, graph.getArcWeight(25, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(30, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(60000, graph.getArcWeight(31, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
        Assert.assertEquals(0, graph.getArcWeight(35, MeasureType.DURATION, MeasureAggregation.MEAN),0.0);
    }
    
//    @Test
//    public void test_BPI12() throws InvalidArcException {
//        ALog log = new ALog(readRealLog_BPI12());
//        AttributeLog attLog = new AttributeLog(log, log.getAttributeStore().getStandardEventConceptName());
//        
//        AttributeLogGraph graph = attLog.getGraphView().getLogGraph();
//        long start = System.currentTimeMillis();
//        graph.setParams(MeasureType.FREQUENCY, MeasureAggregation.TOTAL, false, false);
//        System.out.println(1.0*(System.currentTimeMillis() - start)/1000 + " seconds");
//    }
//    
//    @Test
//    public void test_BPI15() throws InvalidArcException {
//        ALog log = new ALog(readRealLog_BPI15());
//        AttributeLog attLog = new AttributeLog(log, log.getAttributeStore().getStandardEventConceptName());
//        
//        AttributeLogGraph graph = attLog.getGraphView().getLogGraph();
//        long start = System.currentTimeMillis();
//        graph.setParams(MeasureType.FREQUENCY, MeasureAggregation.TOTAL, false, false);
//        System.out.println(1.0*(System.currentTimeMillis() - start)/1000 + " seconds");
//    }
//    
//    @Test
//    public void test_BPI18() throws InvalidArcException {
//        ALog log = new ALog(readRealLog_BPI18());
//        AttributeLog attLog = new AttributeLog(log, log.getAttributeStore().getStandardEventConceptName());
//        
//        AttributeLogGraph graph = attLog.getGraphView().getLogGraph();
//        long start = System.currentTimeMillis();
//        graph.setParams(MeasureType.FREQUENCY, MeasureAggregation.TOTAL, false, false);
//        System.out.println(1.0*(System.currentTimeMillis() - start)/1000 + " seconds");
//    }
    
//    @Test
//    public void test_teys_log() throws InvalidArcException {
//        ALog log = new ALog(readRealLog_teys());
//        AttributeLog attLog = new AttributeLog(log, log.getAttributeStore().getStandardEventConceptName());
//        
//        AttributeLogGraph graph = attLog.getGraphView().getLogGraph();
//        long start = System.currentTimeMillis();
//        graph.setParams(MeasureType.FREQUENCY, MeasureAggregation.TOTAL, false, false);
//        System.out.println(1.0*(System.currentTimeMillis() - start)/1000 + " seconds");
//    }
    
//    @Test
//    public void test_procmin_log() throws InvalidArcException {
//        long start = 0;
//        
//        start = System.currentTimeMillis();
//        ALog log = new ALog(readRealLog_BPI15());
//        AttributeLog attLog = new AttributeLog(log, log.getAttributeStore().getStandardEventConceptName());
//        System.out.println("Build ALog and AttributeLog: " + 1.0*(System.currentTimeMillis() - start)/1000 + " seconds");
//        
//        start = System.currentTimeMillis();
//        AttributeLogGraph graph = attLog.getGraphView().getLogGraph();
//        System.out.println("Build full graph: " + 1.0*(System.currentTimeMillis() - start)/1000 + " seconds");
//        
//        start = System.currentTimeMillis();
//        graph.setParams(MeasureType.FREQUENCY, MeasureAggregation.TOTAL, false, false);
//        System.out.println("Build all sub-graphs: " + 1.0*(System.currentTimeMillis() - start)/1000 + " seconds");
//    }    
}