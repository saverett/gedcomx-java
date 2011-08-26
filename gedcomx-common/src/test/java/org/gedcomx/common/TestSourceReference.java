package org.gedcomx.common;

import org.gedcomx.types.ResourceType;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.ArrayList;

import static org.gedcomx.rt.SerializationUtil.processThroughJson;
import static org.gedcomx.rt.SerializationUtil.processThroughXml;
import static org.testng.AssertJUnit.assertEquals;

/**
 * @author Ryan Heaton
 */
@Test
public class TestSourceReference {

  /**
   * tests source reference xml
   */
  public void testSourceReferenceXml() throws Exception {
    ResourceReference reference = new ResourceReference();
    reference.setResource(URI.create("urn:someid"));
    reference.setKnownType(ResourceType.Collection);
    reference.setId("refid");
    reference.setOtherElements(new ArrayList<Object>());
    reference.getOtherElements().add(new CustomElement("alt"));
    CustomElement custom = new CustomElement();
    custom.setSource(reference);
    custom = processThroughXml(custom);
    assertEquals("urn:someid", custom.getSource().getResource().toString());
    assertEquals(ResourceType.Collection, custom.getSource().getKnownType());
    assertEquals("refid", custom.getSource().getId());
    assertEquals("alt", ((CustomElement) custom.getSource().getOtherElements().get(0)).getId());
  }

  /**
   * tests source reference json
   */
  public void testSourceReferenceJson() throws Exception {
    ResourceReference reference = new ResourceReference();
    reference.setResource(URI.create("urn:someid"));
    reference.setKnownType(ResourceType.Collection);
    reference.setId("refid");
    reference.setOtherElements(new ArrayList<Object>());
    reference.getOtherElements().add(new CustomElement("alt"));
    CustomElement custom = new CustomElement();
    custom.setSource(reference);
    custom = processThroughJson(custom);
    assertEquals("urn:someid", custom.getSource().getResource().toString());
    assertEquals(ResourceType.Collection, custom.getSource().getKnownType());
    assertEquals("refid", custom.getSource().getId());
    assertEquals("alt", ((CustomElement) custom.getSource().getOtherElements().get(0)).getId());
  }

}