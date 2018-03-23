package main.java.com.streamlinq.stream;


/**
 * Created by ZhangYuan on 2018/1/14.
 */

/**
 * 内部用的Set
 * 在万条数据（Debug环境）下取1000条和HashSet对比时间（毫秒）
 * Set：              87、85、86
 * HashSet：     93、91、92
 * 100000条
 * Set：              795、745、750、738、740
 * HashSet：     765、771、761、764、757
 *
 * @param <TElement>
 */

final class Set<TElement>
{
        final static class Slot
        {
                int hashCode;
                Object value;
                int next;
        }

        private int[] buckets;
        private Slot[] slots;

        private int count;
        private int freeList;
        private EqualityComparer<Object> equalityComparer = null;

        Set(EqualityComparer<Object> comparator)
        {
                this.equalityComparer = comparator;
                if(this.equalityComparer == null)
                {
                        this.equalityComparer = DefaultEqualityComparer.instance;
                }

                buckets = new int[7];
                slots = new Slot[7];
                for(int i = 0; i < slots.length; i++)
                {
                        slots[i] = new Slot();
                }
                freeList = -1;
        }

        public Set()
        {
                this(null);
        }


        // If value is not in set, add it and return true; otherwise return false
        public boolean add(TElement value)
        {
                return !find(value, true);
        }

        // Check whether value is in set
        public boolean contains(TElement value)
        {
                return find(value, false);
        }

        // If value is in set, remove it and return true; otherwise return false
        boolean remove(TElement value)
        {
                int hashCode = internalGetHashCode(value);
                int bucket = hashCode % buckets.length;
                int last = -1;
                for(int i = buckets[bucket] - 1; i >= 0; last = i, i = slots[i].next)
                {
                        if(slots[i].hashCode == hashCode && this.equalityComparer.equals(slots[i].value, value))
                        {
                                if(last < 0)
                                {
                                        buckets[bucket] = slots[i].next + 1;
                                }
                                else
                                {
                                        slots[last].next = slots[i].next;
                                }
                                slots[i].hashCode = -1;
                                slots[i].value = null;
                                slots[i].next = freeList;
                                freeList = i;
                                return true;
                        }
                }
                return false;
        }

        private boolean find(TElement value, boolean add)
        {
                int hashCode = internalGetHashCode(value);
                for(int i = buckets[hashCode % buckets.length] - 1; i >= 0; i = slots[i].next)
                {
                        if(slots[i].hashCode == hashCode && this.equalityComparer.equals(slots[i].value, value))
                        {
                                return true;
                        }
                }
                if(add)
                {
                        int index;
                        if(freeList >= 0)
                        {
                                index = freeList;
                                freeList = slots[index].next;
                        }
                        else
                        {
                                if(count == slots.length)
                                {
                                        resize();
                                }
                                index = count;
                                count++;
                        }
                        int bucket = hashCode % buckets.length;
                        slots[index].hashCode = hashCode;
                        slots[index].value = value;
                        slots[index].next = buckets[bucket] - 1;
                        buckets[bucket] = index + 1;
                }
                return false;
        }

        private void resize()
        {
                int newSize = count * 2 + 1;
                int[] newBuckets = new int[newSize];
                Slot[] newSlots = new Slot[newSize];
                System.arraycopy(slots, 0, newSlots, 0, count);
                for(int i = newSlots.length - 1; i >= 0; i--)
                {
                        if(newSlots[i] == null)
                        {
                                newSlots[i] = new Slot();
                        }
                        else
                        {
                                break;
                        }
                }

                for(int i = 0; i < count; i++)
                {
                        int bucket = newSlots[i].hashCode % newSize;
                        newSlots[i].next = newBuckets[bucket] - 1;
                        newBuckets[bucket] = i + 1;
                }
                buckets = newBuckets;
                slots = newSlots;
        }

        private int internalGetHashCode(TElement value)
        {
                return (value == null) ? 0 : this.equalityComparer.hashCode(value) & 0x7FFFFFFF;
        }
}