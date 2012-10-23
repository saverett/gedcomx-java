# The GEDCOM X Conceptual Model

## Status

This document specifies a standard conceptual model for exchanging genealogical data,
and requests discussion and suggestions for improvements.

## Copyright Notice

Copyright 2011-2012 Intellectual Reserve, Inc.

## License

This document is distributed under a Creative Commons Attribution-ShareAlike license.
For details, see:

http://creativecommons.org/licenses/by-sa/3.0/

# 1. Introduction

The GEDCOM X conceptual model is a specification of formal concepts and types
that are used to provide a standard model and vocabulary for describing genealogical
data. Genealogical data is structured by data types such as persons, 
relationships, and sources.

## 1.1 Identifier, Version, and Dependencies

The identifier for this specification is:

`http://gedcomx.org/conceptual-model/v1`

For convenience, the GEDCOM X conceptual model may be referred to as "GEDCOM X 1.0".
This specification uses "GEDCOM X" internally.

This specification is depends on the GEDCOM X Date Model specification identified
by [`http://gedcomx.org/date-model/v1`](https://github.com/FamilySearch/gedcomx/blob/master/specifications/date-model-specification.md).

## 1.2 Notational Conventions

### 1.2.1 Data Types

This specification uses the term "data type" to refer to a formal description of
a data structure, including the properties that define valid instances of the
data type. For example, information about a person might be contained within a 
data structure that supplies the person's name, birth date, and gender. A "data 
type" defines the formal properties of the data structure.

When a property of a data type is specified as being of a particular data type, the
property inherits the corresponding requirements from that data type's definition.
When a data type is specified as an extension of another data type, the extending
data type inherits the corresponding requirements from the extended data type's
definition.

Data types are defined by the following sections:

1. The *identifier* for the data type, which takes the form of a URI.
2. The *extension* of the data type (if any) which specifies which data type
   is extended by the data type.
3. The *properties* of the data type, which define the information the data type
   encapsulates.

<a id="formal-values" />

### 1.2.2 Original, Normalized, and Standardized Values

When a property is identified as an "original value", the value of the property
is interpreted as the literal value supplied by a user. If a property is identified as a
"normalized value", the value of the property is assumed to be formally formatted, either by
a user or by the application, for the purpose of easier processing, such as for display
purposes. When a property has been identified as a "standardized value", the value of the
property resolves a discrete, machine-identifiable value based on a specific standard.

### 1.2.3 Keywords

The key words "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL NOT",
"SHOULD", "SHOULD NOT", "RECOMMENDED", "MAY", and "OPTIONAL" in this
document are to be interpreted as described in BCP 14, 
[RFC2119](http://tools.ietf.org/html/rfc2119), as scoped to those conformance 
targets.

## 1.3 Internationalization Considerations

GEDCOM X must be designed to accommodate users and software of different languages and locales.
To this end, a property named `lang` is supported on relevant GEDCOM X data types. This
attribute is used to identify the locale of the user who provided the data. This property
is optional, and when it is not provided, a processor MAY process the data as if it were
provided in the default locale of the processor. When this property is provided, it overrides
the value of the property supplied by any containing data elements.

The values of the attribute are language identifiers as defined by [IETF BCP 47](http://tools.ietf.org/html/bcp47),
_Tags for the Identification of Languages_; in addition, the empty string may be specified to
explicitly state a processor may process the data as if it were provided in the default locale
of the processor.

In order to prevent undue burden on producers and consumers of GEDCOM X data, not all data types
provide multiple values for properties that are used for user input. For example, the text of
a note is _not_ defined as a list of language-identified strings. However, some cases have been
identified where multi-valued input is needed for the benefit of exchanging genealogical data
across cultural boundaries. Such cases include the need to input multiple name forms and the need
to identify multiple titles for a source.


# 2. Common Data Types

Many data types of the GEDCOM X conceptual model share common structures. This section
defines the data types of those shared structures and their requirements for convenient
reference by the other data type definitions.


<a id="uri"/>

## 2.1 The URI Reference

The Uniform Resource Identifier ("URI") is fundamental to the GEDCOM X conceptual model.
The URI is used to identify specific resources, such as persons, relationships, and 
sources. The URI is also used to identify the data structures that describe those resources
and even the data types that define those data structures. The URI is specified by
[RFC 3986](http://tools.ietf.org/html/rfc3986).

GEDCOM X entities use the URI to reference other entities. For example, a GEDCOM X `Relationship`
identifies a person in the relationship by referencing a URI to the person. When a property
(such as the `person1` property of `Relationship`) is of type `URI`, the value of the property
is interpreted as a "URI Reference" as defined by [RFC 3986, section 4](http://tools.ietf.org/html/rfc3986#section-4).
This specification uses the term "URI" to refer to both a "URI" and a "URI Reference" as
defined by [RFC 3986](http://tools.ietf.org/html/rfc3986).

<a id="identifier-type"/>


## 2.2 The "Identifier" Data Type

The `Identifier` data type defines the data structure used to supply an identifier of a 
genealogical resource in a specific data set.

The `Identifier` data type does NOT support extension properties (see [Extension Properties](#extension-properties)).

### identifier

The identifier for the "Identifier" data type is:

`http://gedcomx.org/v1/Identifier`

### properties

name  | description | data type
------|-------------|----------
value | The value of the identifier. | string (possibly interpreted as a URI, depending on the type of the identifier).
type  | URI identifying the type of the identifier. | [URI](#uri) - MUST resolve to an identifier type. Refer to the list of [known identifier types](#known-identifier-types).

<a id="known-identifier-types"/>

### known identifier types

The following identifier types are defined by GEDCOM X.

URI | description
----|------------
`http://gedcomx.org/Primary` | The primary identifier for the resource.
`http://gedcomx.org/Evidence` | An identifier for the evidence that supports the resource. For example, when a conclusion about a person is extracted, analyzed and evaluated atomically within the context of a single source, it takes the form of a (extracted) person conclusion, and the extracted conclusion may supply an identifier for the person. As all evidence for the person is gathered, the (working) person conclusion identifies the evidence used to support the conclusion by including each evidence identifier in the list of identifiers for the person.
`http://gedcomx.org/Deprecated` | An identifier that has been relegated, deprecated, or otherwise downgraded. This identifier is commonly used as the result of a merge when what was once a primary identifier for a person is no longer primary.
`http://gedcomx.org/Persistent` | An identifier that is considered to be a long-term persistent identifier. Applications that provide persistent identifiers are claiming that links to the resource using the identifier won't break.

### examples

* Person "12345" merges into Person "67890". Person "67890" assumes identifier "12345". Identifier "12345" is of type `http://gedcomx.org/Deprecated`
  because the merged person "12345" now uses identifier "67890".
* An online web application issues a persistent identifier of value `https://familysearch.org/pal:/12345` to a `Person` and the same identifier
  is used as the primary identifier for the `Person`. The list of identifiers for the `Person` contains two identifiers with value `https://familysearch.org/pal:/12345`,
  one of type `http://gedcomx.org/Primary` and one of type `http://gedcomx.org/Persistent`.
* An application allows a researcher to extract information from a single census record about a person. The application assigns an identifier "abcde" to the
  `Person` extracted from the census record. The researcher extracts additional information about the person from a birth certificate and the application
  assigns identifier "fghij" to the `Person` extracted from the birth certificate. As the researcher gathers and analyzes the evidence for the person, the
  application creates a (working) `Person` conclusion that references the census record and the birth certificate as a source. When the researcher concludes
  that person "abcde" and person "fghij" are the same person, the list of identifiers for the working `Person` includes two identifiers of type
  `http://gedcomx.org/Evidence`: "abcde" and "fghij".


<a id="attribution"/>

## 2.3 The "Attribution" Data Type

The `Attribution` data type defines the data structure used to attribute _who_, _when_, and _why_ to
genealogical data. Data is attributed to the agent who made the _latest significant change_ to the nature
of the data being attributed. The definition of a "significant change" is outside the scope of this specification
and is left to the implementer of the application.

#### The Granularity of Attribution

The granularity of data that is attributed varies widely from application to application. Some highly collaborative applications
might take a fine-grained approach, tracking attribution at the level of names, facts, and source references. Single-user
applications might simply provide attribution for a large set of data, such as an entire data tree. GEDCOM X explicitly
defines attribution for top-level entities such as persons, relationships, and documents, but also recognizes attribution
as an extension property that can be applied at a finer level of granularity. (For more information about extension properties,
see Section 6). For all data types where attribution explicitly recognized, it is an OPTIONAL property.

If data is not explicitly attributed, the attribution for the data is assumed to be the attribution for the containing
data. For example, if no attribution is provided for the name of a person (as an extension property of the name), then the
attribution for the name is assumed to be the attribution of the person that contains the name. If no attribution for the
person is provided, the attribution is assumed to be the attribution for the data set that contains the person.

### identifier

The identifier for the "Attribution" data type is:

`http://gedcomx.org/v1/Attribution`

### properties

name  | description | data type
------|-------------|----------
contributor | Reference to the contributor to whom the attributed data is attributed. | [URI](#uri) - MUST resolve to an instance of [`http://gedcomx.org/v1/Agent`](#agent).
modified | Timestamp of when the attributed data was contributed. | timestamp
changeMessage | A statement of why the attributed data is being provided by the contributor. | string

### examples

todo:


<a id="note"/>

## 2.4 The "Note" Data Type

The `Note` data type defines a note that was contributed from genealogical research.

Notes are not intended to contain genealogical conclusions.  Notes are only associated with a single genealogical resource.

### identifier

The identifier for the "Note" data type is:

`http://gedcomx.org/v1/Note`

### properties

name  | description | data type
------|-------------|----------
lang | The locale identifier for the note. | [IETF BCP 47](http://tools.ietf.org/html/bcp47) locale tag
subject | A subject or title for the note. | string
text | The text of the note. | string
attribution | The attribution of this note. | [`http://gedcomx.org/Attribution`](#attribution)


<a id="text-value"/>

## 2.5 The "TextValue" Data Type

The `TextValue` data type defines a literal value.

The `TextValue` data type does NOT support extension properties (see [Extension Properties](#extension-properties)).

### identifier

The identifier for the "TextValue" data type is:

`http://gedcomx.org/v1/TextValue`

### properties

name  | description | data type
------|-------------|----------
lang | The locale identifier for the value of the text. | [IETF BCP 47](http://tools.ietf.org/html/bcp47) locale tag
value | The literal value. | string



# 3. Data Types for Describing Sources

Citing sound evidence is a critical component to the exchange of genealogical data. The source of
a piece of genealogical data has to be completely and accurately described in order to be
consistently evaluated by other genealogical researchers. Many sources do not have a digital
representation (e.g. books or other physical artifacts). Sources are instead _described_
and their _description_ is provided as the digital representation of a source.

This section defines the data types that are used for describing and referencing sources.


<a id="source-description"/>

## 3.1 The "SourceDescription" Data Type

The `SourceDescription` data type defines a description of a source.

### identifier

The identifier for the "SourceDescription" data type is:

`http://gedcomx.org/v1/SourceDescription`

### properties

name | description | data type
-----|-------------|----------
id | An identifier for the data structure holding the source description data. The id is to be used as a "fragment identifier" as defined by [RFC 3986, Section 3.5](http://tools.ietf.org/html/rfc3986#section-3.5). As such, the constraints of the id are provided in the definition of the media type (e.g. XML, JSON) of the data structure. | string
citations | The citations for this source. At least one citation MUST be provided. If more than one citation is provided, citations are assumed to be given in order of preference, with the most preferred citation in the first position in the list. | [`http://gedcomx.org/v1/SourceCitation`](#source-citation) - REQUIRED
about | A uniform resource identifier (URI) for the resource being described. | [URI](#uri) - OPTIONAL
mediator | A reference to the entity that mediates access to the described source. | [URI](#uri) - OPTIONAL; MUST resolve to an instance of [`http://gedcomx.org/v1/Agent`](#agent).
sources | A list of references to any sources from which this source is derived. | List of [`http://gedcomx.org/v1/SourceReference`](#source-reference) - OPTIONAL
extractedConclusions | A list of references to any conclusions that were extracted from this source, to be analyzed and evaluated atomically within on context of the source. | [URI](#uri) - OPTIONAL
componentOf | A reference to the source that contains this source -- its parent context; this is for cases where this description is not complete without the description of its parent context | [`http://gedcomx.org/v1/SourceReference`](#source-reference) - OPTIONAL
titles | The display names for this source. If more than one title is provided, titles are assumed to be given in order of preference, with the most preferred title in the first position in the list. | List of [`http://gedcomx.org/TextValue`](#text-value) - OPTIONAL
notes  | A list of notes about a source. | List of [`http://gedcomx.org/Note`](#note) - OPTIONAL
attribution | The attribution of this source description. | [`http://gedcomx.org/Attribution`](#attribution)

<a id="source-citation"/>

## 3.2 The "SourceCitation" Data Type

The `SourceCitation` data type defines a container for the metadata necessary to identify a source(s)
and from which bibliographic citation strings may be rendered.

### identifier

The identifier for the "SourceCitation" data type is:

`http://gedcomx.org/v1/SourceCitation`

### properties

name | description | data type
-----|-------------|----------
lang | The locale identifier for the citation. | [IETF BCP 47](http://tools.ietf.org/html/bcp47) locale tag
value | A rendering of the full (working) citation as a string. | string - REQUIRED
citationTemplate | The identifier of the citation template by which this citation may be interpreted. | [URI](#uri) - OPTIONAL;  MUST resolve to an instance of [`http://gedcomx.org/v1/CitationTemplate`](#citation-template).
fields  | A list of citation fields about a source. | List of [`http://gedcomx.org/v1/CitationField`](#citation-field) - OPTIONAL

<a id="citation-field"/>

## 3.3 The "CitationField" Data Type

The `CitationField` data type defines a piece of metadata (e.g., author, volume, page, publisher, etc.)
necessary to identify a source.

The `CitationField` data type does NOT support extension properties (see [Extension Properties](#extension-properties)).

### identifier

The identifier for the "CitationField" data type is:

`http://gedcomx.org/v1/CitationField`

### properties

name | description | data type
-----|-------------|----------
name | The identifier for the citation detail -- defined by a citation template or a citation template library. | [URI](#uri) - REQUIRED
value | The value of the citation detail. | string - OPTIONAL


<a id="source-reference"/>

## 3.4 The "SourceReference" Data Type

The `SourceReference` data type defines a reference to a source.  A genealogical conclusion or a
derivate source [the referring object -- the object holding the `SourceReference` instance] cites
its supporting source(s) [the target source(s)] using an instance(s) of `SourceReference`.

### identifier

The identifier for the "SourceReference" data type is:

`http://gedcomx.org/v1/SourceReference`

### properties

name | description | data type
-----|-------------|----------
description  | Reference to a _description_ of the target source. | [URI](#uri) - MUST resolve to an instance of [`http://gedcomx.org/v1/SourceDescription`](#source-description)
attribution | The attribution of this source reference. | [`http://gedcomx.org/Attribution`](#attribution)

### examples

todo:

<a id="citation-template"/>

## 3.5 The "CitationTemplate" Data Type

todo: define citation templates and any associated infrastructure

<a id="note-about-citation-templates"/>
<table>
  <tr>
    <td style="background:#F0F0FF;border-color:#F0F0FF; padding-bottom:0px; font-size:18px">
<b>a note about citation templates (not part of this specification)</b>
    </td>
  </tr>
  <tr>
    <td style="background:#F0F0FF;border-color:#F0F0FF; padding-top:0px">
<p>
Building source citations is said to be an "art" and requires a great deal of flexibility.  While citation style
guides exist (e.g. Chicago style, Turabian, Evidence Style -- see <i>Evidence Explained</i> by Elizabeth Shown Mills,
etc.), they are considered guides and execution within their guidelines allows for flexibility.
No one style has been universally accepted, nor will a style become universally accepted in the forseeable future.  Therefore,
the approach to collecting citation metadata and the exchange of this data needs to support flexibility.
</p><p/><p>
The citation metadata is collected as a set of name-value pairs (see the CitationField data type), but there still remains
a set of difficult questions, including:</p>
<ul>
  <li>What is the set of valid "names" (e.g. controlled vocabulary)?</li>
  <li>What "values" are valid values for a given "name"?</li>
  <li>What fields (name-value pairs) are required, and under what circumstances might they be optional?</li>
  <li>What metadata needs to be collected to properly site data in a specific record?  For example, is a citation for the 1900 US Census
different from a citation for the 1910 US Census and from a citation for the 1911 England and Wales Census?</li>
</ul>
<p/><p>
Further questions remain about how to arrange the fields into citations. For example, given a set of metadata, can it be expressed
in the Chicago style?  The Evidence style?  What about a lesser-used or custom style?
</p><p/><p>
To address these design issues, we define <b><i>citation templates</i></b>.  A citation template defines the metadata that should
be collected for a given source and the semantics associated with that metadata.  Templates also specify how the metadata
is rendered into specific citation styles, such as Chicago style or Evidence style.  Templates could be designed to
address specific record types and may be grouped by type (e.g., a group of templates for census records, or newspapers, or
US census records, or UK census records, etc.).  Templates could also be associated with other templates into libraries
such that a specific piece of citation metadata (e.g., a citation field named "Volume") has a shared semantic meanign across the set
of associated templates -- a template library. Perhaps a standard template library could be developed that would address the most
common citation needs.
</p><p/><p>
Citation templates have not been fully specified and are therefore outlined here in broad terms.  For now, this
section functions as a place holder for the needed specifications.  We will likely consider ideas like those expressed
at http://sourcetemplates.org/ when we define this portion of the specification.
</p>
    </td>
  </tr>
</table>



# 4. Data Types for Describing Contributors

Genealogical research is performed and data is gathered by a living person or organization.
This section describes the data types that are used to provide information about
contributors of genealogical data.

<a id="online-account"/>

## 4.1 The "OnlineAccount" Data Type

The `OnlineAccount` data type defines a description of an account in an online web application.

### identifier

The identifier for the `OnlineAccount` data type is:

`http://gedcomx.org/v1/OnlineAccount`

### properties

name | description | data type
-----|-------------|----------
serviceHomepage  | The home page of the service. | [URI](#uri)
accountName | The name, label, or id associating the owner of the account with the account. | string

<a id="address"/>

## 4.2 The "Address" Data Type

The `Address` data type defines a street address of a person or organization.

### identifier

The identifier for the `Address` data type is:

`http://gedcomx.org/v1/Address`

### properties

name | description | data type
-----|-------------|----------
value | A string representation of the value. Used when the address isn't separated into its constituent parts. | string
city | The city. | string
country | The country. | string
postalCode | The postal code. | string
stateOrProvince | The state or province. | string
street | The street. | string
street2 | The street (second line). | string
street3 | The street (third line). | string


<a id="agent"/>

## 4.3 The "Agent" Data Type

The `Agent` data type defines a living entity, such as a person (user) or organization.

### identifier

The identifier for the `Agent` data type is:

`http://gedcomx.org/v1/Agent`

### properties

name | description | data type
-----|-------------|----------
id | An identifier for the data structure holding the agent data. The id is to be used as a "fragment identifier" as defined by [RFC 3986, Section 3.5](http://tools.ietf.org/html/rfc3986#section-3.5). As such, the constraints of the id are provided in the definition of the media type (e.g. XML, JSON) of the data structure. | string
identifiers | Identifiers for the agent. When an identifier for an agent is also an identifier for a person, the data in the person describes the agent. | List of [`http://gedcomx.org/v1/Identifier`](#identifier-type). Order is preserved.
names | The names of the person or organization. If more than one name is provided, names are assumed to be given in order of preference, with the most preferred name in the first position in the list. | List of [`http://gedcomx.org/TextValue`](#text-value)
homepage | The homepage of the person or organization. | [URI](#uri)
openid  | The [openid](http://openid.net/) of the person or organization. | [URI](#uri)
accounts  | The online accounts of the person or organization. | List of [`http://gedcomx.org/v1/OnlineAccount`](#online-account). Order is preserved.
emails  | The email addresses of the person or organization. | List of [URI](#uri) - MUST resolve to a valid e-mail address (e.g. "mailto:someone@gedcomx.org"). Order is preserved.
phones  | The phones (voice, fax, mobile) of the person or organization. | List of [URI](#uri) - MUST resolve to a valid phone number (e.g. "tel:+1-201-555-0123"). Order is preserved.
addresses  | The addresses of the person or organization. | List of [`http://gedcomx.org/v1/Address`](#address). Order is preserved.



# 5. Data Types for Describing Conclusions

This section describes the data types that are used to define genealogical conclusions.


<a id="conclusion"/>

## 5.1 The "Conclusion" Data Type

The `Conclusion` data type defines the base conceptual model for basic genealogical conclusions.

### identifier

The identifier for the `Conclusion` data type is:

`http://gedcomx.org/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
id | An identifier for the data structure holding the conclusion data. The id is to be used as a "fragment identifier" as defined by [RFC 3986, Section 3.5](http://tools.ietf.org/html/rfc3986#section-3.5). As such, the constraints of the id are provided in the definition of the media type (e.g. XML, JSON) of the data structure. | string
lang | The locale identifier for the conclusion. | [IETF BCP 47](http://tools.ietf.org/html/bcp47) locale tag
confidence  | Reference to the confidence level of the conclusion. | [URI](#uri) - MUST resolve to a confidence level. Refer to the list of [known confidence levels](#known-confidence-levels).
sources | The list of references to the sources of related to this conclusion. The sources of a conclusion MUST also be sources of the conclusion's containing entity (i.e. [`Person`](#person) or [`Relationship`](#relationship) ).| List of [`http://gedcomx.org/v1/SourceReference`](#source-reference). Order is preserved.
notes  | A list of notes about a conclusion. | List of [`http://gedcomx.org/Note`](#note) - OPTIONAL

<a id="known-confidence-levels"/>

### known confidence levels

The following confidence levels are defined by GEDCOM X. For more information, refer to
Mills, Elizabeth Shown. "Fundamentals of Evidence Analysis." <i>Evidence Explained.</i> 2nd ed.
(Baltimore, Maryland: Genealogical Publishing Company, 2009), 19-20 (Section 1.6).

URI | description
----|------------
`http://gedcomx.org/Certainly`|The contributor has no reasonable doubt about the assertion, based upon sound research and good evidence.
`http://gedcomx.org/Probably`|The contributor feels the assertion is more likely than not, based upon sound research and good evidence.
`http://gedcomx.org/Possibly`|The contributor feels some evidence supports the assertion, but the assertion is far from proved.
`http://gedcomx.org/Likely`|The contributor feels the odds weigh at least slightly in favor of the assertion.
`http://gedcomx.org/Apparently`|The contributor has formed an impression or presumption, typically based upon common experience, but has not tested the matter.
`http://gedcomx.org/Perhaps`|The contributor suggests that an idea is plausible, although it remains to be tested.

<a id="document"/>

## 5.2 The "Document" Data Type

The `Document` data type defines the base conceptual model for genealogical conclusions that are managed as textual documents.  The `Document` data type extends the `Conclusion` data type.

### identifier

The identifier for the `Document` data type is:

`http://gedcomx.org/v1/Document`

### extension

This data type extends the following data type:

`http://gedcomx.org/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
type | URI identifying the type of the document. | [URI](#uri) - MUST resolve to a document type. Refer to the list of [known document types](#known-document-types).
text | The text of the document. | string
attribution | The attribution of the document. | [`http://gedcomx.org/Attribution`](#attribution)

<a id="known-document-types"/>

### known document types

The following gender types are defined by GEDCOM X:

URI | description
----|-------------
`http://gedcomx.org/Abstract` | The document is an abstract of a record or document.
`http://gedcomx.org/Transcription` | The document is a transcription of a record or document.
`http://gedcomx.org/Translation` | The document is a translation of a record or document.
`http://gedcomx.org/Analysis` | The document is an analysis done by a researcher, often used as a genealogical proof statement.

<a id="gender-conclusion"/>

## 5.3 The "Gender" Data Type

The `Gender` data type defines a conclusion about the gender of a person. the `Gender` data type
extends the `Conclusion` data type.

### identifier

The identifier for the `Gender` data type is:

`http://gedcomx.org/v1/Gender`

### extension

This data type extends the following data type:

`http://gedcomx.org/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
type | URI identifying the type of the gender. | [URI](#uri) - MUST resolve to a gender type. Refer to the list of [known gender types](#known-gender-types).

<a id="known-gender-types"/>

### known gender types

The following gender types are defined by GEDCOM X:

URI | description
----|-------------
`http://gedcomx.org/Male`|
`http://gedcomx.org/Female`|
`http://gedcomx.org/Unknown`|


<a id="name-conclusion"/>

## 5.4 The "Name" Data Type

The `Name` data type defines a conclusion about a name of a person. The `Name` data type extends the `Conclusion` data type.

A `Name` is intended to represent a single variant of a person's name.  This means that nicknames, spelling variations, or other name variants (often distinguishable by a name type) should be modeled with separate instances of `Name`.

The name forms of a name contain representations of this name.  A `Name` MUST contain at least one name form, usually a representation of the name that is considered proper and well formed in the person's native, historical cultural context.  Other name forms MAY be included, which can be used to represent this name in contexts where the native name form is not easily recognized and interpreted.  Alternate forms are more likely in situations where conclusions are being analyzed across cultural context boundaries that have both language and writing script differences.

For example, a Korean name has a native Korean form, but can also have a Chinese form and a Roman/Latin form -- three different name forms, but each representing the same name.

If more than one name form is provided, included name forms are assumed to be given in order of preference, with the most preferred name form in the first position in the list.

### identifier

The identifier for the `Name` data type is:

`http://gedcomx.org/v1/Name`

### extension

This data type extends the following data type:

`http://gedcomx.org/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
type | URI identifying the type of the name. | [URI](#uri) - MUST resolve to a name type. Refer to the list of [known name types](#known-name-types).
preferred | Whether this name is preferred above the other `Name` conclusions of a person. | boolean
date | The date of applicability of the name. | [`http://gedcomx.org/v1/Date`](#conclusion-date)
nameForms | The name form(s) that best represents this name `NameForm` -- usually representations considered proper and well formed in the person's native, historical cultural context. All included name forms should be representations of the same name -- __*not*__ name variants (e.g., nicknames, spelling variations). | List of [`http://gedcomx.org/v1/NameForm`](#name-form). Order is preserved.

### examples

Consider the following: a Russian person with the birth name "Александр" (rendered as "Alexander" in English and in a Latin script) that also went by this name's common nickname, "Саша" (rendered as "Sasha" in English).

It is tempting to think that this situation should be modeled with one `Name` instance that has several alternate `NameForm`s.  The model is __*not*__ intended to be used in this way. Instead, this person's names ought to be modeled such that the
birth name and the nickname are modeled as two separate `Name` instances: one instance for the birth name, and one for the nickname.  The `type` property MAY be provided to distinguish the birth name from the nickname.
Each `Name` instance MAY have two `NameForm` instances: one with the native form of the name and another with the alternate form.  Using an informal pseudo code, it might look something like the following:

```
Name1.type=http://gedcomx.org/BirthName
Name1.preferred=true
Name1.nameForms[0].fullText=Александр
Name1.nameForms[1].fullText=Alexander

Name2.type=http://gedcomx.org/Nickname
Name2.preferred=false
Name2.nameForms[0].fullText=Саша
Name2.nameForms[1].fullText=Sasha
```
<a id="known-name-types"/>

### known name types

The following name types are defined by GEDCOM X:

URI | description
----|-------------
`http://gedcomx.org/BirthName` | Name given at birth.
`http://gedcomx.org/DeathName` | Name used at the time of death.
`http://gedcomx.org/MarriedName` | Name accepted at marriage.
`http://gedcomx.org/AlsoKnownAs` | "Also known as" name.
`http://gedcomx.org/Nickname`| Nickname.
`http://gedcomx.org/AdoptiveName` | Name given at adoption.
`http://gedcomx.org/FormalName` | A formal name, usually given to distinguish it from a name more commonly used.
`http://gedcomx.org/ReligiousName` | A name given at a religious rite or ceremony.


<a id="fact-conclusion"/>

## 5.5 The "Fact" Data Type

The `Fact` data type defines a conclusion about a fact of the life of a person or
the nature of a relationship. The `Fact` data type extends the `Conclusion` data type.

### identifier

The identifier for the `Fact` data type is:

`http://gedcomx.org/v1/Fact`

### extension

This data type extends the following data type:

`http://gedcomx.org/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
type | URI identifying the type of the fact. | [URI](#uri) - MUST resolve to a fact type. See the list of [known fact types](#known-fact-types).
date | The date of applicability of the fact. | [`http://gedcomx.org/v1/Date`](#conclusion-date)
place | The place of applicability of the fact. | [`http://gedcomx.org/v1/Place`](#conclusion-place)
value | The original value of the fact as supplied by the contributor. | string

<a id="known-fact-types"/>

### known fact types

The following fact types are defined by GEDCOM X:

URI | description | scope
----|-------------|------
`http://gedcomx.org/Adoption`| A fact of a person's adoption. In the context of a parent-child relationship, it describes a fact of the adoption of a child by a parent. | person
`http://gedcomx.org/AdultChristening`| A fact of a person's christening or baptism as an adult. | person
`http://gedcomx.org/Baptism`| A fact of a person's baptism. | person
`http://gedcomx.org/BarMitzvah`| A fact of a person's bar mitzvah. | person
`http://gedcomx.org/BatMitzvah`| A fact of a person's bat mitzvah. | person
`http://gedcomx.org/Birth`| A fact of a person's birth. In the context of a parent-child relationship, it describes a fact of the biological birth of a child to a parent. | person
`http://gedcomx.org/Blessing`| A fact of an official blessing received by a person, such as at the hands of a clergy member or at another religious rite. | person
`http://gedcomx.org/Burial`| A fact of the burial of person's body after death. | person
`http://gedcomx.org/Caste`| A fact of a person's caste. | person
`http://gedcomx.org/Census`| A fact of a person's participation in a census. | person
`http://gedcomx.org/Christening`| A fact of a person's christening *at birth*. Note: use `AdultChristening` for the christening as an adult. | person
`http://gedcomx.org/Circumcision`| A fact of a person's circumcision. | person
`http://gedcomx.org/Clan`| A fact of a person's clan. | person
`http://gedcomx.org/Confirmation`| A fact of a person's confirmation (or other rite of initiation) in a church or religion. | person
`http://gedcomx.org/Cremation`| A fact of the cremation of person's body after death. | person
`http://gedcomx.org/Death`| A fact of the death of a person. | person
`http://gedcomx.org/Education`| A fact of an education or an educational achievement (e.g. diploma, graduation, scholarship, etc.) of a person. | person
`http://gedcomx.org/Emigration`| A fact of the emigration of a person. | person
`http://gedcomx.org/Ethnicity`| A fact of a person's ethnicity or race. | person
`http://gedcomx.org/Excommunication`| A fact of a person's excommunication from a church. | person
`http://gedcomx.org/FirstCommunion`| A fact of a person's first communion in a church. | person
`http://gedcomx.org/Funeral`| A fact of a person's funeral. | person
`http://gedcomx.org/Immigration`| A fact of a person's immigration. | person
`http://gedcomx.org/LandTransation`| A fact of a land transaction enacted by a person. | person
`http://gedcomx.org/Living`| A fact of a record of a person's living for a specific period. This is designed to include "flourish", defined to mean the time period in an adult's life where he was most productive, perhaps as a writer or member of the state assembly. It does not reflect the person's birth and death dates. | person
`http://gedcomx.org/MaritalStatus`| A fact of a person's marital status. | person
`http://gedcomx.org/Medical`| A fact of a person's medical record, such as for an illness or hospital stay. | person
`http://gedcomx.org/MilitaryAward`| A fact of a person's military award. | person
`http://gedcomx.org/MilitaryDischarge`| A fact of a person's military discharge. | person
`http://gedcomx.org/MilitaryService`| A fact of a person's militray service. | person
`http://gedcomx.org/Mission`| A fact of a person's church mission. | person
`http://gedcomx.org/MoveTo`| A fact of a person's move (i.e. change of residence) to a new location. | person
`http://gedcomx.org/MoveFrom`| A fact of a person's move (i.e. change of residence) from a location. | person
`http://gedcomx.org/MultipleBirth`| A fact that a person was born as part of a multiple birth (e.g. twin, triplet, etc.) | person
`http://gedcomx.org/NationalId`| A fact of a person's national id (e.g. social security number). | person
`http://gedcomx.org/Nationality`| A fact of a person's nationality. | person
`http://gedcomx.org/Naturalization`| A fact of a person's naturalization (i.e. acquisition of citizenship and nationality). | person
`http://gedcomx.org/NumberOfChildren`| A fact of the number of children of a person or relationship. | person
`http://gedcomx.org/NumberOfMarriages`| A fact of a person's number of marriages. | person
`http://gedcomx.org/Occupation`| A fact of a person's occupation or employment. | person
`http://gedcomx.org/Ordination`| A fact of a person's ordination to a stewardship in a church. | person
`http://gedcomx.org/PhysicalDescription`| A fact of a person's physical description. | person
`http://gedcomx.org/Probate`| A fact of a receipt of probate of a person's property. | person
`http://gedcomx.org/Property`| A fact of a person's property or possessions. | person
`http://gedcomx.org/Religion`| A fact of a person's religion. | person
`http://gedcomx.org/Residence`| A fact of a person's residence. | person
`http://gedcomx.org/Retirement`| A fact of a person's retirement. | person
`http://gedcomx.org/Stillbirth`| A fact of a person's stillbirth. | person
`http://gedcomx.org/Will`| A fact of a person's will. | person
`http://gedcomx.org/Visit`| A fact of a person's visit to a place different from the person's residence. | person
`http://gedcomx.org/Annulment`| The fact of an annulment of a marriage. | couple relationship
`http://gedcomx.org/CommonLawMarriage`| The fact of a marriage by common law. | couple relationship
`http://gedcomx.org/Divorce`| The fact of a divorce of a couple. | couple relationship
`http://gedcomx.org/DivorceFiling`| The fact of a filing for divorce. | couple relationship
`http://gedcomx.org/Engagement`| The fact of an engagement to be married. | couple relationship
`http://gedcomx.org/Marriage`| The fact of a marriage. | couple relationship
`http://gedcomx.org/MarriageBanns`| The fact of a marriage banns. | couple relationship
`http://gedcomx.org/MarriageContract`| The fact of a marriage contract. | couple relationship
`http://gedcomx.org/MarriageLicense`| The fact of a marriage license. | couple relationship
`http://gedcomx.org/MarriageNotice`| The fact of a marriage notice. | couple relationship
`http://gedcomx.org/NumberOfChildren`| A fact of the number of children of a person or relationship. | couple relationship
`http://gedcomx.org/Separation`| A fact of a couple's separation. | couple relationship
`http://gedcomx.org/Adoption`| A fact of a person's adoption. In the context of a parent-child relationship, it describes a fact of the adoption of a child by a parent. | parent-child relationship
`http://gedcomx.org/Birth`| A fact of a person's birth. In the context of a parent-child relationship, it describes a fact of the biological birth of a child to a parent. | parent-child relationship
`http://gedcomx.org/Foster`| A fact about a foster relationship between a foster parent and a child. | parent-child relationship
`http://gedcomx.org/Guardianship`| A fact about a legal guardianship between a parent and a child. | parent-child relationship
`http://gedcomx.org/Step`| A fact about the step relationship between a parent and a child. | parent-child relationship


<a id="person"/>

## 5.6 The "Person" Data Type

The `Person` data type defines a description of a person. The `Person` data type
extends the `Conclusion` data type.

### identifier

The identifier for the `Person` data type is:

`http://gedcomx.org/v1/Person`

### extension

This data type extends the following data type:

`http://gedcomx.org/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
identifiers | Identifiers for the person. | List of [`http://gedcomx.org/v1/Identifier`](#identifier-type). Order is preserved.
living | Whether the person is considered living. | boolean
gender | The conclusion about the gender of the person. | [`http://gedcomx.org/v1/Gender`](#gender)
names | The conclusions about the names of the person. | List of [`http://gedcomx.org/v1/Name`](#name-conclusion). Order is preserved.
facts | The conclusions about the facts of the life of the person. | List of [`http://gedcomx.org/v1/Fact`](#fact-conclusion). Order is preserved.
attribution | The attribution of the person. | [`http://gedcomx.org/Attribution`](#attribution)


<a id="relationship"/>

## 5.7 The "Relationship" Data Type

The `Relationship` data type describes the relationship between two persons. The `Relationship` data type
extends the `Conclusion` data type.

### identifier

The identifier for the `Relationship` data type is:

`http://gedcomx.org/v1/Relationship`

### extension

This data type extends the following data type:

`http://gedcomx.org/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
type | URI identifying the type of the relationship. | [URI](#uri) - MUST resolve to a relationship type. Refer to the list of [known relationship types](#known-relationship-types)
person1 | Reference to the first person in the relationship. | [URI](#uri) - MUST resolve to an instance of [`http://gedcomx.org/v1/Person`](#person)
person2 | Reference to the second person in the relationship. | [URI](#uri) - MUST resolve to an instance of [`http://gedcomx.org/v1/Person`](#person)
facts | The conclusions about the facts of the life of the relationship. | List of [`http://gedcomx.org/v1/Fact`](#fact-conclusion). Order is preserved.
attribution | The attribution of the relationship. | [`http://gedcomx.org/Attribution`](#attribution)

Note: when a relationship type implies direction, the relationship is said to
be *from* person1 *to* person2. For example, in a parent-child relationship, the
`person1` property refers to the parent and the `person2` property refers to the
child.

<a id="known-relationship-types"/>

### known relationship types

The following relationship types are defined by GEDCOM X:

URI | description
----|-------------
`http://gedcomx.org/Couple`|
`http://gedcomx.org/ParentChild`|


<a id="conclusion-event-role"/>

## 5.8 The "EventRole" Data Type

The `EventRole` data type defines a role played in an event by a person.  The `EventRole` data type extends the `Conclusion` data type.

### identifier

The identifier for the `EventRole` data type is:

`http://gedcomx.org/v1/EventRole`

### extension

This data type extends the following data type:

`http://gedcomx.org/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
person | Reference to the person playing the role in the event. | [`URI`](#uri) - MUST resolve to an instance of [`http://gedcomx.org/v1/Person`](#person)
type | Reference to the role type. | [`URI`](#uri) - MUST resolve to a role type. Refer to the list of [known role types](#known-roles).
details | Details about the role of the person in the event. | string

<a id="known-roles"/>

### known role types

The following role types are defined by GEDCOM X:

URI | description
----|------------
`http://gedcomx.org/Principal`|
`http://gedcomx.org/Participant`|
`http://gedcomx.org/Official`|
`http://gedcomx.org/Witness`|


<a id="event"/>

## 5.9 The "Event" Data Type

The `Event` data type defines a description of a historical event. The `Event` data type
extends the `Conclusion` data type.

### identifier

The identifier for the `Event` data type is:

`http://gedcomx.org/v1/Event`

### extension

This data type extends the following data type:

`http://gedcomx.org/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
type | URI identifying the type of the event. | [URI](#uri). MUST resolve to an event type. Refer to the list of [known event types](#known-event-types)
date | The date of the event. | [`http://gedcomx.org/v1/Date`](#conclusion-date)
place | The place of the event. | [`http://gedcomx.org/v1/Place`](#conclusion-place)
roles | The roles of the persons in the event. | List of [`http://gedcomx.org/v1/EventRole`](#conclusion-event-role). Order is preserved.
attribution | The attribution of the event. | [`http://gedcomx.org/Attribution`](#attribution)

<a id="known-event-types"/>

### known event types

The following event types are defined by GEDCOM X:

URI | description
----|------------
`http://gedcomx.org/Adoption` | An adoption event.
`http://gedcomx.org/AdultChristening` | An adult christening event.
`http://gedcomx.org/Annulment` | An annulment event of a marriage.
`http://gedcomx.org/Baptism` | A baptism event.
`http://gedcomx.org/BarMitzvah` | A bar mitzvah event.
`http://gedcomx.org/BatMitzvah` | A bat mitzvah event.
`http://gedcomx.org/Birth` | A birth event.
`http://gedcomx.org/Blessing` | A an official blessing event, such as at the hands of a clergy member or at another religious rite.
`http://gedcomx.org/Burial` | A burial event.
`http://gedcomx.org/Census` | A census event.
`http://gedcomx.org/Christening` | A christening event *at birth*. Note: use `AdultChristening` for a christening event as an adult.
`http://gedcomx.org/Circumcision` | A circumcision event.
`http://gedcomx.org/Confirmation` | A confirmation event (or other rite of initiation) in a church or religion.
`http://gedcomx.org/Cremation` | A cremation event after death.
`http://gedcomx.org/Death` | A death event.
`http://gedcomx.org/Divorce` | A divorce event.
`http://gedcomx.org/DivorceFiling` | A divorce filing event.
`http://gedcomx.org/Education` | A education or an educational achievement event (e.g. diploma, graduation, scholarship, etc.).
`http://gedcomx.org/Engagement` | An engagement to be married event.
`http://gedcomx.org/Emigration` | An emigration event.
`http://gedcomx.org/Excommunication` | An excommunication event from a church.
`http://gedcomx.org/FirstCommunion` | A first communion event.
`http://gedcomx.org/Funeral` | A funeral event.
`http://gedcomx.org/Immigration` | An immigration event.
`http://gedcomx.org/LandTransation` | A land transaction event.
`http://gedcomx.org/Marriage` | A marriage event.
`http://gedcomx.org/MilitaryAward` | A military award event.
`http://gedcomx.org/MilitaryDischarge` | A military discharge event.
`http://gedcomx.org/Mission` | A mission event.
`http://gedcomx.org/MoveFrom` | An event of a move (i.e. change of residence) from a location.
`http://gedcomx.org/MoveTo` | An event of a move (i.e. change of residence) to a location.
`http://gedcomx.org/Naturalization` | A naturalization event (i.e. acquisition of citizenship and nationality).
`http://gedcomx.org/Ordination` | An ordination event.
`http://gedcomx.org/Retirement` | A retirement event.

<a id="conclusion-date"/>

## 5.10 The "Date" Data Type

The `Date` data type defines the value of a genealogical date.

### identifier

The identifier for the `Date` data type is:

`http://gedcomx.org/v1/Date`

### properties

name | description | data type
-----|-------------|----------
original | The original value of the date as supplied by the contributor. | string
formal | The standardized [formal value](#formal-values) of the date, formatted per GEDCOM X Date Format specification. | [GEDCOM X Date](https://github.com/FamilySearch/gedcomx/blob/master/specifications/date-model-specification.md)


<a id="conclusion-place"/>

## 5.11 The "Place" Data Type

The `Place` data type defines the value of a genealogical place.

### identifier

The identifier for the `Place` data type is:

`http://gedcomx.org/v1/Place`

### properties

name | description | data type
-----|-------------|----------
original | The original value of the place as supplied by the contributor. | string
normal | The normalized value of the place. | string
resource | Reference to the standardized resource describing the place. | [URI](#uri)


<a id="name-part"/>

## 5.12 The "NamePart" Data Type

The `NamePart` data type is used to model a portion of a full name, including the terms that make up that portion, and perhaps a name part qualifier (e.g., "given name" or "surname").

A name part value MAY contain more than one term from the full name, such as in the name part "John Fitzgerald" from the full name "John Fitzgerald Kennedy".  If multiple terms are
detailed in a single `NamePart`, these terms are separated using the name separator appropriate to the locale of the name form.

### identifier

The identifier for the `NamePart` data type is:

`http://gedcomx.org/v1/NamePart`

### properties

name | description | data type
-----|-------------|----------
type | URI identifying the type of the name part. | [URI](#uri) - If present, MUST resolve to a name part type. Refer to the list of [known name part types](#known-name-part-types).
value | The term(s) from the name that make up this name part. | string
qualifiers | Type qualifiers to further describe the type of the name part. | List of [URI](#uri) - If present, MUST resolve to a name part type. Refer to the list of [known name part types qualifiers](#known-name-part-qualifier-types).

<a id="known-name-part-types"/>

### known name part types

The following name part types are defined by GEDCOM X:

URI | description
----|-------------
`http://gedcomx.org/Prefix`|
`http://gedcomx.org/Suffix`|
`http://gedcomx.org/Given`|
`http://gedcomx.org/Surname`|

<a id="known-name-part-qualifier-types"/>

### known name part qualifier types

The following name part qualifier types are defined by GEDCOM X:

URI | description
----|-------------
`http://gedcomx.org/Title`|A designation for honorifics (e.g. Dr., Rev., His Majesty, Haji), ranks (e.g. Colonel, General, Knight, Esquire), positions (e.g. Count, Chief, Father, King) or other titles (e.g., PhD, MD)
`http://gedcomx.org/Primary`|A designation for the name of most prominent in importance among the names of that type (e.g., the primary given name).
`http://gedcomx.org/Secondary`|A designation for a name that is not primary in its importance among the names of that type (e.g., a secondary given name).
`http://gedcomx.org/Middle`|A designation useful for cultures that designate a middle name that is distinct from a given name and a surname.
`http://gedcomx.org/Familiar`|A designation for one's familiar name.
`http://gedcomx.org/Religious`|A designation for a name given for religious purposes.
`http://gedcomx.org/Family`|A name that associates a person with a group, such as a clan, tribe, or patriarchal hierarchy.
`http://gedcomx.org/Maiden`|A designation given by women to their original surname after they adopt a new surname upon marriage.
`http://gedcomx.org/Patronymic`|A name derived from a father or paternal ancestor.
`http://gedcomx.org/Matronymic`|A name derived from a mother or maternal ancestor.
`http://gedcomx.org/Geographic`|A name derived from associated geography.
`http://gedcomx.org/Occupational`|A name derived from one's occupation.
`http://gedcomx.org/Characteristic`|A name derived from a characteristic.
`http://gedcomx.org/Postnom`|A name mandedated by law populations from Congo Free State / Belgian Congo / Congo / Democratic Republic of Congo (formerly Zaire).
`http://gedcomx.org/Particle`|A grammatical designation for articles (a, the, dem, las, el, etc.), prepositions (of, from, aus, zu, op, etc.), initials (e.g. PhD, MD), annotations (e.g. twin, wife of, infant, unknown), comparators (e.g. Junior, Senior, younger, little), ordinals (e.g. III, eighth), and conjunctions (e.g. and, or, nee, ou, y, o, ne, &amp;).


<a id="name-form"/>

## 5.13 The "NameForm" Data Type

The `NameForm` data type defines a representation of a name (a "name form") within a given cultural context, such as a given language and script.

As names are captured (in records or in applications), the terms in the name are sometimes classified by type.  For example, a certificate of death might prompt for "given name(s)" and "surname". The `parts` list can be used to represent the terms in the name that have been classified.

If both a full rendering of the name and a list of parts are provided, there is no requirement that every term in the fully rendered name appear in the list of parts.

Name parts in the `parts` list are to be ordered in the natural order they would be spoken in the given cultural context.

If a full rendering of the name is not provided (i.e., the name has only been expressed in `parts`), a full rendering of the name can be derived (sans punctuation) by concatenating, in order, each `NamePart.value` in the `parts` list, separating each part with the name part delimiter appropriate for the given `locale`.

### identifier

The identifier for the `NameForm` data type is:

`http://gedcomx.org/v1/NameForm`

### properties

name | description | data type
-----|-------------|----------
lang | The locale identifier for the name form. | [IETF BCP 47](http://tools.ietf.org/html/bcp47) locale tag
fullText | A full rendering of the name (or as much of the name as is known) with the terms in the name given in the natural order they would be spoken in the applicable cultural context. | string
parts | The parts of the name form, ordered in the natural order they would be spoken in the given cultural context. | List of [`http://gedcomx.org/v1/NamePart`](#name-part). Order is preserved.

### examples

Consider the following: the Russian name "Пётр Ильи́ч Чайко́вский" in the Cyrillic script, its Latin-script equivalent "Pyotr Ilyich Tchaikovsky", and its anglicised equivalent "Peter Ilyich Tchaikovsky".  Using an informal pseudo code, these name forms might be modeled as follows:

```
NameForm1.locale=ru-Cyrl
NameForm1.fullText=Пётр Ильи́ч Чайко́вский
NameForm1.parts[0].type=http://gedcomx.org/Given
NameForm1.parts[0].value=Пётр
NameForm1.parts[0].qualifiers[0]=http://gedcomx.org/First
NameForm1.parts[1].type=http://gedcomx.org/Middle
NameForm1.parts[1].value=Ильи́ч
NameForm1.parts[1].qualifiers[0]=http://gedcomx.org/Middle
NameForm1.parts[2].type=http://gedcomx.org/Surname
NameForm1.parts[2].value=Чайко́вский

NameForm2.locale=ru-Latn
NameForm2.fullText=Pyotr Ilyich Tchaikovsky
NameForm2.parts[0].type=http://gedcomx.org/Given
NameForm2.parts[0].value=Pyotr
NameForm2.parts[0].qualifiers[0]=http://gedcomx.org/First
NameForm2.parts[1].type=http://gedcomx.org/Given
NameForm2.parts[1].value=Ilyich
NameForm2.parts[1].qualifiers[0]=http://gedcomx.org/Middle
NameForm2.parts[2].type=http://gedcomx.org/Surname
NameForm2.parts[2].value=Tchaikovsky

NameForm3.locale=en-Latn
NameForm3.fullText=Peter Ilyich Tchaikovsky
NameForm3.parts[0].type=http://gedcomx.org/Given
NameForm3.parts[0].value=Peter
NameForm3.parts[0].qualifiers[0]=http://gedcomx.org/First
NameForm3.parts[1].type=http://gedcomx.org/Given
NameForm3.parts[1].value=Ilyich
NameForm3.parts[1].qualifiers[0]=http://gedcomx.org/Middle
NameForm3.parts[2].type=http://gedcomx.org/Surname
NameForm3.parts[2].value=Tchaikovsky
```


# 6. Extensibility

## Extensions from Non-GEDCOM X Vocabularies

This specification provides the data types and properties that define GEDCOM X.
Other data types and vocabularies ("foreign vocabularies") can be used in
GEDCOM X data types.

## Extensions to the GEDCOM X Vocabulary

The GEDCOM X namespaces are reserved for future forward-compatible revisions
of GEDCOM X. Future versions of this specification could add new data types and
properties to the GEDCOM X vocabulary.  Software written to conform to this
version of the specification will not be able to process such extensions correctly
and, in fact, will not be able to distinguish such extensions from error.  For
the purposes of this discussion, unrecognized data types and properties from the
GEDCOM X vocabulary will be considered "foreign vocabularies".

## Processing Foreign Vocabularies

GEDCOM X Processors that encounter foreign vocabularies in a location that is
legal according to this specification MUST NOT stop processing or
signal an error.  It might be the case that the GEDCOM X Processor is
able to process the foreign vocabularies correctly and does so.  Otherwise,
such vocabularies are termed "unknown foreign vocabularies".

When unknown foreign vocabularies are encountered, GEDCOM X Processors MAY
bypass the foreign properties, type references, and textual content and
MUST NOT change their behavior as a result of the presence of the vocabulary.

<a id="extension-properties"/>

## Extension Properties

GEDCOM X allows properties of foreign vocabularies in any data type, except
where it is explicitly forbidden.

## Extension Types

GEDCOM X allows data types of foreign vocabularies to be referenced anywhere
data types are defined to be referenced. Examples of data type references include
name types, fact types, resource types, etc. GEDCOM X supplies its own set of
"known" data types and implementors are encouraged to use the list of known data
types as much as possible to preserve maximum compatibility.

## User-Defined Types

Some applications MAY allow data types to be provided. For example, a genealogy
application may provide a feature that allows a user to supply a "custom" fact
about the life of a person when the list of known fact types doesn't contain
the type of fact the user may want to add.

In the case where a user has supplied a title or description instead of selecting
a known data type, GEDCOM X recognizes the data URI scheme as defined by
[RFC 2397](http://tools.ietf.org/html/rfc2397).

# 7. Miscellaneous To Do

todo: add details about which properties are required.

todo: supply details about how GEDCOM X defines its evidence model.