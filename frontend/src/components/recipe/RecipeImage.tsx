/* eslint-disable react/require-default-props */
/* eslint-disable react/jsx-no-useless-fragment */
import { ChangeEvent, useEffect, useState } from 'react'
import * as S from '../../styles/recipe/RecipeImage.styled'
import useIcon from '../../hooks/useIcon'
import ModalWithoutButton from '../ui/ModalWithoutButton'

// size : 해당 component width, height (1:1)
// isText : '대표 이미지를 등록해주세요' 텍스트 노출 여부
export interface ImageFile {
  mainImage: File
  cookingOrderImageList: File[]
}

interface RecipeImageProps {
  size: number
  isText: boolean
  formKey: string
  imageFile: ImageFile
  setImageFile: (value: ImageFile) => void
  defaultUrl?: string
}

const RecipeImage = ({
  size,
  isText,
  formKey,
  imageFile,
  setImageFile,
  defaultUrl,
}: RecipeImageProps) => {
  const { IcAddRoundDuotone } = useIcon()

  const [imageUrl, setImageUrl] = useState('')
  const [isModal, setIsModal] = useState(false)

  const uploadImage = (e: ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]

    if (file && file.size > 500000) {
      setIsModal(true)
      setTimeout(() => {
        setIsModal(false)
      }, 1500)
      return
    }
    if (file) {
      if (formKey === 'cookingOrderImageList') {
        const updatedCookingOrderImageList = [
          ...imageFile.cookingOrderImageList,
          file,
        ]
        setImageFile({
          ...imageFile,
          cookingOrderImageList: updatedCookingOrderImageList,
        })
      } else {
        setImageFile({ ...imageFile, [formKey]: file })
      }

      const imageURL = URL.createObjectURL(file)
      setImageUrl(imageURL)
    }
  }

  useEffect(() => {
    if (defaultUrl) {
      setImageUrl(defaultUrl)
    }
  }, [defaultUrl])

  return (
    <>
      {imageUrl ? (
        <S.Image referrerPolicy="no-referrer" src={imageUrl} $size={size} />
      ) : (
        <>
          <S.Label htmlFor="file" $size={size}>
            <IcAddRoundDuotone size={4} />
            {isText && <S.SubText>대표 이미지를 등록해주세요.</S.SubText>}
          </S.Label>
          <S.ImageButton
            id="file"
            accept="image/*"
            type="file"
            onChange={uploadImage}
            $size={size}
          />
        </>
      )}
      {isModal && (
        <ModalWithoutButton
          content="파일은 500kb 이하여야합니다."
          setIsModalFalse={() => setIsModal(false)}
        />
      )}
    </>
  )
}

export default RecipeImage
