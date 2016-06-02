# body-processing

This is a library for transforming content in xml format in the UP platform.

`XMLEventHandler`s can be registered in the `XMLEventHandlerRegistry` which during its processing will make changes
to the original structure accordingly.

`XMLEventHandler`s can be registered for many event types, such as characters, comments or start and end elements.
Start and end elements (for example `<ft-content> or <a>` can have multiple handlers registered with different attributes
or other settings.