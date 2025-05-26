/*
 Copyright 2006-2011 Abdulla Abdurakhmanov (abdulla@latestbit.com)
 Original sources are available at www.latestbit.com

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
using System;
using System.Reflection;
using System.IO;
using org.bn.attributes;
using org.bn.attributes.constraints;
using org.bn.coders;
using org.bn.metadata;

namespace org.bn.metadata.constraints
{
    public class ASN1ValueRangeConstraintMetadata : IASN1ConstraintMetadata 
    {
        private long minValue, maxValue;
        private bool isExtensible = false;

        public ASN1ValueRangeConstraintMetadata(ASN1ValueRangeConstraint annotation) 
        {
            this.minValue = annotation.Min;
            this.maxValue = annotation.Max;
            this.isExtensible = annotation.IsExtensible;
        }
        
        public long Min {
            get { return minValue; }
        }
        
        public long Max {
            get { return maxValue; }
        }

        public bool IsExtensible
        {
            get { return isExtensible; }
        }

        public bool checkIsExtended(long value)
        {
            return (value > maxValue || value < minValue);
        }

        public bool checkValue(long value) 
        {
            return isExtensible || !checkIsExtended(value);
        }
    }
}
