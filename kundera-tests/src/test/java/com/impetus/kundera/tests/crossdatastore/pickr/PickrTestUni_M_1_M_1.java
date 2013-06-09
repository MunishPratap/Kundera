/*******************************************************************************
 * * Copyright 2012 Impetus Infotech.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 ******************************************************************************/
package com.impetus.kundera.tests.crossdatastore.pickr;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.cassandra.thrift.CfDef;
import org.apache.cassandra.thrift.ColumnDef;
import org.apache.cassandra.thrift.IndexType;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KsDef;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SchemaDisagreementException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.impetus.kundera.tests.cli.CassandraCli;
import com.impetus.kundera.tests.crossdatastore.pickr.entities.album.AlbumUni_M_1_M_1;
import com.impetus.kundera.tests.crossdatastore.pickr.entities.photo.PhotoUni_M_1_M_1;
import com.impetus.kundera.tests.crossdatastore.pickr.entities.photographer.PhotographerUni_M_1_M_1;

/**
 * @author amresh.singh
 * 
 */
public class PickrTestUni_M_1_M_1 extends PickrBaseTest
{
    private static Logger log = LoggerFactory.getLogger(PickrTestUni_M_1_M_1.class);

    @Before
    public void setUp() throws Exception
    {
        log.info("Executing PICKR Test: " + this.getClass().getSimpleName() + "\n======"
                + "==========================================================");
        super.setUp();
    }

    /**
     * Tear down.
     * 
     * @throws Exception
     *             the exception
     */
    @After
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Test.
     */
    @Test
    public void test()
    {
        executeTests();
    }

    @Override
    public void addPhotographer()
    {
        List<PhotographerUni_M_1_M_1> ps = populatePhotographers();

        for (PhotographerUni_M_1_M_1 p : ps)
        {
            pickr.addPhotographer(p);
        }
    }

    @Override
    protected void getPhotographer()
    {
        PhotographerUni_M_1_M_1 p1 = (PhotographerUni_M_1_M_1) pickr.getPhotographer(PhotographerUni_M_1_M_1.class, 1);
        assertPhotographer(p1, 1);

        PhotographerUni_M_1_M_1 p2 = (PhotographerUni_M_1_M_1) pickr.getPhotographer(PhotographerUni_M_1_M_1.class, 2);
        assertPhotographer(p2, 2);

        PhotographerUni_M_1_M_1 p3 = (PhotographerUni_M_1_M_1) pickr.getPhotographer(PhotographerUni_M_1_M_1.class, 3);
        assertPhotographer(p3, 3);

        PhotographerUni_M_1_M_1 p4 = (PhotographerUni_M_1_M_1) pickr.getPhotographer(PhotographerUni_M_1_M_1.class, 4);
        assertPhotographer(p4, 4);
    }

    @Override
    protected void updatePhotographer()
    {
        PhotographerUni_M_1_M_1 p1 = (PhotographerUni_M_1_M_1) pickr.getPhotographer(PhotographerUni_M_1_M_1.class, 1);
        assertPhotographer(p1, 1);
        p1.setPhotographerName("Amresh2");

        pickr.mergePhotographer(p1);

        PhotographerUni_M_1_M_1 p1Modified = (PhotographerUni_M_1_M_1) pickr.getPhotographer(
                PhotographerUni_M_1_M_1.class, 1);
        assertModifiedPhotographer(p1Modified, 1);

        PhotographerUni_M_1_M_1 p2 = (PhotographerUni_M_1_M_1) pickr.getPhotographer(PhotographerUni_M_1_M_1.class, 2);
        assertPhotographer(p2, 2);

        p2.setPhotographerName("Vivek2");
        pickr.mergePhotographer(p2);

        PhotographerUni_M_1_M_1 p2Modified = (PhotographerUni_M_1_M_1) pickr.getPhotographer(
                PhotographerUni_M_1_M_1.class, 2);
        assertModifiedPhotographer(p2Modified, 2);

        PhotographerUni_M_1_M_1 p3 = (PhotographerUni_M_1_M_1) pickr.getPhotographer(PhotographerUni_M_1_M_1.class, 3);
        assertPhotographer(p3, 3);
        p3.setPhotographerName("Kuldeep2");

        pickr.mergePhotographer(p3);

        PhotographerUni_M_1_M_1 p3Modified = (PhotographerUni_M_1_M_1) pickr.getPhotographer(
                PhotographerUni_M_1_M_1.class, 3);
        assertModifiedPhotographer(p3Modified, 3);

        PhotographerUni_M_1_M_1 p4 = (PhotographerUni_M_1_M_1) pickr.getPhotographer(PhotographerUni_M_1_M_1.class, 4);
        assertPhotographer(p4, 4);

        p4.setPhotographerName("VivekS2");
        pickr.mergePhotographer(p4);

        PhotographerUni_M_1_M_1 p4Modified = (PhotographerUni_M_1_M_1) pickr.getPhotographer(
                PhotographerUni_M_1_M_1.class, 4);
        assertModifiedPhotographer(p4Modified, 4);
    }

    @Override
    protected void getAllPhotographers()
    {
        List<Object> ps = pickr.getAllPhotographers(PhotographerUni_M_1_M_1.class.getSimpleName());

        for (Object p : ps)
        {
            PhotographerUni_M_1_M_1 pp = (PhotographerUni_M_1_M_1) p;
            Assert.assertNotNull(pp);
            assertModifiedPhotographer(pp, pp.getPhotographerId());
        }

    }

    @Override
    protected void deletePhotographer()
    {
        PhotographerUni_M_1_M_1 p1 = (PhotographerUni_M_1_M_1) pickr.getPhotographer(PhotographerUni_M_1_M_1.class, 1);
        assertModifiedPhotographer(p1, 1);

        pickr.deletePhotographer(p1);

        PhotographerUni_M_1_M_1 p1AfterDeletion = (PhotographerUni_M_1_M_1) pickr.getPhotographer(
                PhotographerUni_M_1_M_1.class, 1);
        Assert.assertNull(p1AfterDeletion);

        PhotographerUni_M_1_M_1 p2 = (PhotographerUni_M_1_M_1) pickr.getPhotographer(PhotographerUni_M_1_M_1.class, 2);
        Assert.assertNotNull(p2);

        pickr.deletePhotographer(p2);

        PhotographerUni_M_1_M_1 p2AfterDeletion = (PhotographerUni_M_1_M_1) pickr.getPhotographer(
                PhotographerUni_M_1_M_1.class, 2);
        Assert.assertNull(p2AfterDeletion);

        PhotographerUni_M_1_M_1 p3 = (PhotographerUni_M_1_M_1) pickr.getPhotographer(PhotographerUni_M_1_M_1.class, 3);
        Assert.assertNotNull(p3);

        pickr.deletePhotographer(p3);

        PhotographerUni_M_1_M_1 p3AfterDeletion = (PhotographerUni_M_1_M_1) pickr.getPhotographer(
                PhotographerUni_M_1_M_1.class, 3);
        Assert.assertNull(p3AfterDeletion);

        PhotographerUni_M_1_M_1 p4 = (PhotographerUni_M_1_M_1) pickr.getPhotographer(PhotographerUni_M_1_M_1.class, 4);
        Assert.assertNotNull(p4);

        pickr.deletePhotographer(p4);

        PhotographerUni_M_1_M_1 p4AfterDeletion = (PhotographerUni_M_1_M_1) pickr.getPhotographer(
                PhotographerUni_M_1_M_1.class, 4);
        Assert.assertNull(p4AfterDeletion);

    }

    private void assertPhotographer(PhotographerUni_M_1_M_1 p, int photographerId)
    {

        if (photographerId == 1)
        {
            Assert.assertNotNull(p);
            Assert.assertEquals(1, p.getPhotographerId());
            Assert.assertEquals("Amresh", p.getPhotographerName());

            Assert.assertNotNull(p.getAlbum());
            AlbumUni_M_1_M_1 album = p.getAlbum();
            Assert.assertNotNull(album);
            Assert.assertTrue(album.getAlbumId().equals("album_1"));
            Assert.assertEquals("My Phuket Vacation", album.getAlbumName());
            Assert.assertEquals("Went Phuket with friends", album.getAlbumDescription());

            PhotoUni_M_1_M_1 albumPhoto = album.getPhoto();
            Assert.assertNotNull(albumPhoto);

            Assert.assertNotNull(albumPhoto);
            Assert.assertTrue(albumPhoto.getPhotoId().equals("photo_1"));

        }
        else if (photographerId == 2)
        {
            Assert.assertNotNull(p);
            Assert.assertEquals(2, p.getPhotographerId());
            Assert.assertEquals("Vivek", p.getPhotographerName());

            Assert.assertNotNull(p.getAlbum());
            AlbumUni_M_1_M_1 album = p.getAlbum();
            Assert.assertNotNull(album);
            Assert.assertTrue(album.getAlbumId().equals("album_1"));
            Assert.assertEquals("My Phuket Vacation", album.getAlbumName());
            Assert.assertEquals("Went Phuket with friends", album.getAlbumDescription());

            PhotoUni_M_1_M_1 albumPhoto = album.getPhoto();
            Assert.assertNotNull(albumPhoto);

            Assert.assertNotNull(albumPhoto);
            Assert.assertTrue(albumPhoto.getPhotoId().equals("photo_1"));
        }
        else if (photographerId == 3)
        {
            Assert.assertNotNull(p);
            Assert.assertEquals(3, p.getPhotographerId());
            Assert.assertEquals("Kuldeep", p.getPhotographerName());

            Assert.assertNotNull(p.getAlbum());
            AlbumUni_M_1_M_1 album = p.getAlbum();
            Assert.assertNotNull(album);
            Assert.assertTrue(album.getAlbumId().equals("album_2"));
            Assert.assertEquals("My Shimla Vacation", album.getAlbumName());
            Assert.assertEquals("Went Shimla with friends", album.getAlbumDescription());

            PhotoUni_M_1_M_1 albumPhoto = album.getPhoto();
            Assert.assertNotNull(albumPhoto);

            Assert.assertNotNull(albumPhoto);
            Assert.assertTrue(albumPhoto.getPhotoId().equals("photo_1"));
        }
        else if (photographerId == 4)
        {
            Assert.assertNotNull(p);
            Assert.assertEquals(4, p.getPhotographerId());
            Assert.assertEquals("VivekS", p.getPhotographerName());

            Assert.assertNotNull(p.getAlbum());
            AlbumUni_M_1_M_1 album = p.getAlbum();
            Assert.assertNotNull(album);
            Assert.assertTrue(album.getAlbumId().equals("album_2"));
            Assert.assertEquals("My Shimla Vacation", album.getAlbumName());
            Assert.assertEquals("Went Shimla with friends", album.getAlbumDescription());

            PhotoUni_M_1_M_1 albumPhoto = album.getPhoto();
            Assert.assertNotNull(albumPhoto);

            Assert.assertNotNull(albumPhoto);
            Assert.assertTrue(albumPhoto.getPhotoId().equals("photo_1"));
        }
        else
        {
            Assert.fail("Invalid Photographer ID: " + photographerId);
        }

    }

    private void assertModifiedPhotographer(PhotographerUni_M_1_M_1 p, int photographerId)
    {

        if (photographerId == 1)
        {
            Assert.assertNotNull(p);
            Assert.assertEquals(1, p.getPhotographerId());
            Assert.assertEquals("Amresh2", p.getPhotographerName());

            Assert.assertNotNull(p.getAlbum());
            AlbumUni_M_1_M_1 album = p.getAlbum();
            Assert.assertNotNull(album);
            Assert.assertTrue(album.getAlbumId().equals("album_1"));
            Assert.assertEquals("My Phuket Vacation", album.getAlbumName());
            Assert.assertEquals("Went Phuket with friends", album.getAlbumDescription());

            PhotoUni_M_1_M_1 albumPhoto = album.getPhoto();
            Assert.assertNotNull(albumPhoto);

            Assert.assertNotNull(albumPhoto);
            Assert.assertTrue(albumPhoto.getPhotoId().equals("photo_1"));

        }
        else if (photographerId == 2)
        {
            Assert.assertNotNull(p);
            Assert.assertEquals(2, p.getPhotographerId());
            Assert.assertEquals("Vivek2", p.getPhotographerName());

            Assert.assertNotNull(p.getAlbum());
            AlbumUni_M_1_M_1 album = p.getAlbum();
            Assert.assertNotNull(album);
            Assert.assertTrue(album.getAlbumId().equals("album_1"));
            Assert.assertEquals("My Phuket Vacation", album.getAlbumName());
            Assert.assertEquals("Went Phuket with friends", album.getAlbumDescription());

            PhotoUni_M_1_M_1 albumPhoto = album.getPhoto();
            Assert.assertNotNull(albumPhoto);

            Assert.assertNotNull(albumPhoto);
            Assert.assertTrue(albumPhoto.getPhotoId().equals("photo_1"));
        }
        else if (photographerId == 3)
        {
            Assert.assertNotNull(p);
            Assert.assertEquals(3, p.getPhotographerId());
            Assert.assertEquals("Kuldeep2", p.getPhotographerName());

            Assert.assertNotNull(p.getAlbum());
            AlbumUni_M_1_M_1 album = p.getAlbum();
            Assert.assertNotNull(album);
            Assert.assertTrue(album.getAlbumId().equals("album_2"));
            Assert.assertEquals("My Shimla Vacation", album.getAlbumName());
            Assert.assertEquals("Went Shimla with friends", album.getAlbumDescription());

            PhotoUni_M_1_M_1 albumPhoto = album.getPhoto();
            Assert.assertNotNull(albumPhoto);

            Assert.assertNotNull(albumPhoto);
            Assert.assertTrue(albumPhoto.getPhotoId().equals("photo_1"));
        }
        else if (photographerId == 4)
        {
            Assert.assertNotNull(p);
            Assert.assertEquals(4, p.getPhotographerId());
            Assert.assertEquals("VivekS2", p.getPhotographerName());

            Assert.assertNotNull(p.getAlbum());
            AlbumUni_M_1_M_1 album = p.getAlbum();
            Assert.assertNotNull(album);
            Assert.assertTrue(album.getAlbumId().equals("album_2"));
            Assert.assertEquals("My Shimla Vacation", album.getAlbumName());
            Assert.assertEquals("Went Shimla with friends", album.getAlbumDescription());

            PhotoUni_M_1_M_1 albumPhoto = album.getPhoto();
            Assert.assertNotNull(albumPhoto);

            Assert.assertNotNull(albumPhoto);
            Assert.assertTrue(albumPhoto.getPhotoId().equals("photo_1"));
        }
        else
        {
            Assert.fail("Invalid Photographer ID: " + photographerId);
        }

    }

    private List<PhotographerUni_M_1_M_1> populatePhotographers()
    {
        List<PhotographerUni_M_1_M_1> photographers = new ArrayList<PhotographerUni_M_1_M_1>();

        // Photographer 1
        PhotographerUni_M_1_M_1 p1 = new PhotographerUni_M_1_M_1();
        p1.setPhotographerId(1);
        p1.setPhotographerName("Amresh");

        AlbumUni_M_1_M_1 album1 = new AlbumUni_M_1_M_1("album_1", "My Phuket Vacation", "Went Phuket with friends");

        AlbumUni_M_1_M_1 album2 = new AlbumUni_M_1_M_1("album_2", "My Shimla Vacation", "Went Shimla with friends");

        PhotoUni_M_1_M_1 photo = new PhotoUni_M_1_M_1("photo_1", "One beach", "On beach with friends");
        album1.setPhoto(photo);
        album2.setPhoto(photo);

        p1.setAlbum(album1);

        // Photographer 2
        PhotographerUni_M_1_M_1 p2 = new PhotographerUni_M_1_M_1();
        p2.setPhotographerId(2);
        p2.setPhotographerName("Vivek");

        p2.setAlbum(album1);

        // Photographer 3
        PhotographerUni_M_1_M_1 p3 = new PhotographerUni_M_1_M_1();
        p3.setPhotographerId(3);
        p3.setPhotographerName("Kuldeep");

        p3.setAlbum(album2);

        // Photographer 4
        PhotographerUni_M_1_M_1 p4 = new PhotographerUni_M_1_M_1();
        p4.setPhotographerId(4);
        p4.setPhotographerName("VivekS");

        p4.setAlbum(album2);

        photographers.add(p1);
        photographers.add(p2);
        photographers.add(p3);
        photographers.add(p4);

        return photographers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.impetus.kundera.tests.crossdatastore.pickr.PickrBaseTest#startServer
     * ()
     */
    @Override
    protected void createCassandraSchema() throws IOException, TException, InvalidRequestException,
            UnavailableException, TimedOutException, SchemaDisagreementException
    {
        /**
         * schema generation for cassandra.
         * */

        KsDef ksDef = null;

        CfDef pCfDef = new CfDef();
        pCfDef.name = "PHOTOGRAPHER";
        pCfDef.keyspace = "Pickr";
        pCfDef.setComparator_type("UTF8Type");
        pCfDef.setDefault_validation_class("UTF8Type");
        ColumnDef pColumnDef2 = new ColumnDef(ByteBuffer.wrap("PHOTOGRAPHER_NAME".getBytes()), "UTF8Type");
        pColumnDef2.index_type = IndexType.KEYS;
        ColumnDef pColumnDef5 = new ColumnDef(ByteBuffer.wrap("ALBUM_ID".getBytes()), "UTF8Type");
        pColumnDef5.index_type = IndexType.KEYS;
        pCfDef.addToColumn_metadata(pColumnDef2);
        pCfDef.addToColumn_metadata(pColumnDef5);

        CfDef aCfDef = new CfDef();
        aCfDef.name = "ALBUM";
        aCfDef.keyspace = "Pickr";
        aCfDef.setComparator_type("UTF8Type");
        aCfDef.setDefault_validation_class("UTF8Type");
        ColumnDef columnDef = new ColumnDef(ByteBuffer.wrap("ALBUM_NAME".getBytes()), "UTF8Type");
        columnDef.index_type = IndexType.KEYS;
        ColumnDef columnDef3 = new ColumnDef(ByteBuffer.wrap("ALBUM_DESC".getBytes()), "UTF8Type");
        columnDef3.index_type = IndexType.KEYS;
        ColumnDef columnDef5 = new ColumnDef(ByteBuffer.wrap("PHOTO_ID".getBytes()), "UTF8Type");
        columnDef5.index_type = IndexType.KEYS;

        aCfDef.addToColumn_metadata(columnDef);
        aCfDef.addToColumn_metadata(columnDef3);
        aCfDef.addToColumn_metadata(columnDef5);

        CfDef photoLinkCfDef = new CfDef();
        photoLinkCfDef.name = "PHOTO";
        photoLinkCfDef.keyspace = "Pickr";
        photoLinkCfDef.setComparator_type("UTF8Type");
        photoLinkCfDef.setDefault_validation_class("UTF8Type");
        ColumnDef columnDef1 = new ColumnDef(ByteBuffer.wrap("PHOTO_CAPTION".getBytes()), "UTF8Type");
        columnDef1.index_type = IndexType.KEYS;
        ColumnDef columnDef2 = new ColumnDef(ByteBuffer.wrap("PHOTO_DESC".getBytes()), "UTF8Type");
        columnDef2.index_type = IndexType.KEYS;

        photoLinkCfDef.addToColumn_metadata(columnDef1);
        photoLinkCfDef.addToColumn_metadata(columnDef2);

        List<CfDef> cfDefs = new ArrayList<CfDef>();
        cfDefs.add(pCfDef);
        cfDefs.add(aCfDef);
        cfDefs.add(photoLinkCfDef);
        try
        {
            ksDef = CassandraCli.client.describe_keyspace("Pickr");
            CassandraCli.client.set_keyspace("Pickr");
            List<CfDef> cfDefn = ksDef.getCf_defs();

            for (CfDef cfDef1 : cfDefn)
            {

                if (cfDef1.getName().equalsIgnoreCase("PHOTOGRAPHER"))
                {
                    CassandraCli.client.system_drop_column_family("PHOTOGRAPHER");
                }
                if (cfDef1.getName().equalsIgnoreCase("ALBUM"))
                {
                    CassandraCli.client.system_drop_column_family("ALBUM");
                }
                if (cfDef1.getName().equalsIgnoreCase("PHOTO"))
                {
                    CassandraCli.client.system_drop_column_family("PHOTO");
                }
            }
            CassandraCli.client.system_add_column_family(pCfDef);
            CassandraCli.client.system_add_column_family(aCfDef);
            CassandraCli.client.system_add_column_family(photoLinkCfDef);
        }
        catch (NotFoundException e)
        {
            addKeyspace(ksDef, cfDefs);
        }
        catch (InvalidRequestException e)
        {
            log.error(e.getMessage());
        }
        catch (TException e)
        {
            log.error(e.getMessage());
        }
    }

}
