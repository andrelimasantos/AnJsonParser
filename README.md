# AnJsonParser
Annotated Java JSON parser 

##How to use AnJsonParser
Any object annotated  with `@JSONType` is eligible  to be serialized or parsed.

Use `@JSONAttribute` annotation in fields to parse or serialize them. Attributes w/o `@JSONAttribute` not will be processed.