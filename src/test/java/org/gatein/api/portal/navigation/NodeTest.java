/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.gatein.api.portal.navigation;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;


/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class NodeTest
{
   //TODO: More tests

   @Test(expected = IllegalArgumentException.class)
   public void test_NullName()
   {
      new Node((String) null);
   }

   @Test(expected = NullPointerException.class)
   public void testNewNode_NullNode()
   {
      new Node((Node) null);
   }

   @Test
   public void testEquals()
   {
      Node foo = new Node("foo");
      Node foo2 = new Node("foo");
      Node bar = new Node("bar");

      assertFalse(foo.equals(bar));
      assertTrue(foo.equals(foo2));
   }

   @Test
   public void testCopy()
   {
      Node original = new Node("foo");
      Node copy = new Node(original);
      assertEquals(original, copy);
   }

   @Test
   public void testCopy_Rename()
   {
      Node original = new Node("foo");
      original.addNode(new Node("1"));
      original.addNode(new Node("2"));

      Node copy = new Node("bar", original);
      assertEquals("bar", copy.getName());
      assertEquals(copy.getNodes(), original.getNodes());
   }

   @Test
   public void testAdd()
   {
      Node parent = new Node("root");
      Node child = new Node("child");
      parent.addNode(child);

      assertEquals(1, parent.getNodes().size());
      assertTrue(parent == child.getParent());
   }

   @Test(expected = NullPointerException.class)
   public void testAdd_NullChild()
   {
      Node parent = new Node("parent");
      parent.addNode(null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAdd_SameChild()
   {
      Node parent = new Node("parent");
      parent.addNode(new Node("child"));
      parent.addNode(new Node("child"));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAdd_ChildWithParent()
   {
      Node parent = new Node("parent");
      Node parent2 = new Node("parent2");
      Node child = new Node("child");
      parent2.addNode(child);

      parent.addNode(child);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAdd_Self()
   {
      Node node = new Node("node");
      node.addNode(node);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAdd_Cyclic()
   {
      Node parent = new Node("parent");
      Node child1 = new Node("child1");
      parent.addNode(child1);
      child1.addNode(parent);
   }

   @Test
   public void testUri() throws Exception
   {
      Node parent = new Node("parent");
      parent.setBaseURI(uri("/portal/classic/"));
      parent.addNode(new Node("child"));

      assertEquals(uri("/portal/classic/parent/child/"), parent.getNode("child").getURI());
   }

   @Test
   public void testUri_NoTrailingSlash() throws Exception
   {
      Node parent = new Node("parent");
      parent.setBaseURI(uri("/portal/classic"));
      parent.addNode(new Node("child"));

      assertEquals(uri("/portal/classic/parent/child/"), parent.getNode("child").getURI());
   }

   @Test
   public void testUri_Copy() throws Exception
   {
      Node parent = new Node("parent");
      parent.setBaseURI(uri("/portal/classic/"));
      parent.addNode(new Node("child"));

      parent = new Node(parent);
      assertEquals(uri("/portal/classic/parent/child/"), parent.getNode("child").getURI());
   }

   @Test
   public void testUri_Copy_Rename() throws Exception
   {
      Node parent = new Node("parent");
      parent.setBaseURI(uri("/portal/classic"));
      parent.addNode(new Node("child"));

      parent = new Node("foo", parent);
      assertEquals(uri("/portal/classic/foo/child/"), parent.getNode("child").getURI());
   }

   private static URI uri(String path) throws URISyntaxException
   {
      return new URI("http", null, "localhost", 8080, path, null, null);
   }
}